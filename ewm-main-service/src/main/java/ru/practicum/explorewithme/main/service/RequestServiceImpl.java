package ru.practicum.explorewithme.main.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.main.dto.*;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.ForbiddenException;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.mapper.RequestMapper;
import ru.practicum.explorewithme.main.model.*;
import ru.practicum.explorewithme.main.repository.EventRepository;
import ru.practicum.explorewithme.main.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RequestServiceImpl implements RequestService {

    private final ParticipationRequestRepository repo;
    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final RequestMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findForRequester(Long userId) {
        return mapper.toDto(repo.findAllByRequesterId(userId));
    }

    @Override
    public ParticipationRequestDto add(Long userId, Long eventId) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event"));
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("user"));

        if (event.getInitiator().getId().equals(userId))
            throw new ConflictException("initiator cannot request");

        if (event.getState() != EventState.PUBLISHED)
            throw new ConflictException("event not published");

        if (event.getParticipantLimit() != 0 &&
            event.getConfirmedRequestsCount() >= event.getParticipantLimit())
            throw new ConflictException("participant limit reached");

        if (repo.existsByRequesterIdAndEventId(userId, eventId))
            throw new ConflictException("duplicate request");

        boolean unlimited        = event.getParticipantLimit() == 0;
        boolean needsModeration  = event.getRequestModeration();
        RequestStatus status     = (unlimited || !needsModeration)
                ? RequestStatus.CONFIRMED
                : RequestStatus.PENDING;

        ParticipationRequest pr = ParticipationRequest.builder()
                .requester(user)
                .event(event)
                .status(status)
                .created(LocalDateTime.now())
                .build();

        return mapper.toDto(repo.save(pr));
    }

    @Override
    public ParticipationRequestDto cancel(Long userId, Long requestId) {
        ParticipationRequest pr = repo.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("request"));
        pr.setStatus(RequestStatus.CANCELED);
        return mapper.toDto(pr);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipationRequestDto> findForEventOwner(Long userId, Long eventId) {
        Event event = getEventOwned(userId, eventId);
        return mapper.toDto(repo.findAllByEventId(event.getId()));
    }

    @Override
    public ParticipationRequestDto confirm(Long userId, Long eventId, Long reqId) {

        Event event = getEventOwned(userId, eventId);
        ParticipationRequest pr = repo.findById(reqId)
                .orElseThrow(() -> new NotFoundException("request"));

        if (!pr.getEvent().getId().equals(event.getId()))
            throw new ConflictException("request not for this event");

        if (pr.getStatus() != RequestStatus.PENDING)
            throw new ConflictException("already handled");

        int limit       = event.getParticipantLimit() == null ? 0 : event.getParticipantLimit();
        int confirmedSoFar = event.getConfirmedRequestsCount();

        if (limit != 0 && confirmedSoFar >= limit)
            throw new ConflictException("limit reached");

        pr.setStatus(RequestStatus.CONFIRMED);
        repo.save(pr);

        if (limit != 0 && confirmedSoFar + 1 >= limit) {
            List<ParticipationRequest> toReject =
                    repo.findAllByEventIdAndStatus(eventId, RequestStatus.PENDING);

            toReject.forEach(r -> r.setStatus(RequestStatus.REJECTED));
            repo.saveAll(toReject);
        }

        return mapper.toDto(pr);
    }

    @Override
    public ParticipationRequestDto reject(Long userId, Long eventId, Long reqId) {
        getEventOwned(userId, eventId);
        ParticipationRequest pr = repo.findById(reqId)
                .orElseThrow(() -> new NotFoundException("request"));

        if (pr.getStatus() != RequestStatus.PENDING)
            throw new ConflictException("already handled");

        pr.setStatus(RequestStatus.REJECTED);
        return mapper.toDto(pr);
    }

    @Transactional
    @Override
    public EventRequestStatusUpdateResult updateStatus(Long userId,
                                                       Long eventId,
                                                       UpdateEventRequestStatusRequest body) {

        Event event = getEventOwned(userId, eventId);

        List<ParticipationRequest> requests = repo.findAllById(body.getRequestIds());
        if (requests.size() != body.getRequestIds().size())
            throw new NotFoundException("some requests not found");

        requests.stream()
                .filter(r -> r.getStatus() != RequestStatus.PENDING)
                .findAny()
                .ifPresent(r -> { throw new ConflictException("non-pending request in list"); });

        int limit           = event.getParticipantLimit() == null ? 0 : event.getParticipantLimit();
        int confirmedSoFar  = event.getConfirmedRequestsCount();
        int slotsLeft       = (limit == 0) ? Integer.MAX_VALUE : limit - confirmedSoFar;

        if (body.getStatus() == RequestStatus.CONFIRMED && requests.size() > slotsLeft)
            throw new ConflictException("participant limit would be exceeded");

        List<ParticipationRequestDto> confirmed = new ArrayList<>();
        List<ParticipationRequestDto> rejected  = new ArrayList<>();

        for (ParticipationRequest r : requests) {

            if (body.getStatus() == RequestStatus.CONFIRMED) {
                r.setStatus(RequestStatus.CONFIRMED);
                confirmed.add(mapper.toDto(r));
            } else {
                r.setStatus(RequestStatus.REJECTED);
                rejected.add(mapper.toDto(r));
            }
        }
        repo.saveAll(requests);

        if (limit != 0 && body.getStatus() == RequestStatus.CONFIRMED
            && confirmedSoFar + confirmed.size() >= limit) {

            List<ParticipationRequest> toReject =
                    repo.findAllByEventIdAndStatus(eventId, RequestStatus.PENDING);

            toReject.forEach(r -> r.setStatus(RequestStatus.REJECTED));
            repo.saveAll(toReject);
        }

        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(confirmed)
                .rejectedRequests(rejected)
                .build();
    }

    private Event getEventOwned(Long ownerId, Long eventId) {
        Event ev = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event"));
        if (!ev.getInitiator().getId().equals(ownerId))
            throw new ForbiddenException("not owner");
        return ev;
    }
}