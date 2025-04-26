package ru.practicum.explorewithme.main.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.dto.*;
import ru.practicum.explorewithme.main.exception.BadRequestException;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.ForbiddenException;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.mapper.EventMapper;
import ru.practicum.explorewithme.main.model.*;
import ru.practicum.explorewithme.main.repository.*;
import ru.practicum.explorewithme.stats.client.StatsClient;
import ru.practicum.explorewithme.stats.common.dto.EndpointHit;
import ru.practicum.explorewithme.stats.common.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {

    private static final int DEFAULT_SIZE = 10;
    private static final Sort SORT_BY_DATE_DESC  = Sort.by(Sort.Direction.DESC, "eventDate");
    private static final Sort SORT_BY_VIEWS_DESC = Sort.by(Sort.Direction.DESC, "views");
    private static final LocalDateTime FAR_PAST    = LocalDateTime.of(1970, 1, 1, 0, 0);
    private static final LocalDateTime FAR_FUTURE  = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

    private final EventRepository eventRepo;
    private final CategoryRepository categoryRepo;
    private final UserRepository userRepo;
    private final ParticipationRequestRepository participationRequestRepo;
    private final StatsClient stats;
    private final EventMapper mapper;

    @Value("${app.name}")
    private String appName;

    @Override
    public List<EventShortDto> searchPublished(String text,
                                               List<Long> categories,
                                               Boolean paid,
                                               LocalDateTime rangeStart,
                                               LocalDateTime rangeEnd,
                                               boolean onlyAvailable,
                                               String sort,
                                               int from,
                                               int size,
                                               HttpServletRequest req) {

        size = normaliseSize(size);
        from = Math.max(from, 0);

        LocalDateTime start = rangeStart != null ? rangeStart : LocalDateTime.now();
        LocalDateTime end   = rangeEnd   != null ? rangeEnd   : FAR_FUTURE;

        if (start.isAfter(end)) {
            throw new BadRequestException("rangeStart must be before rangeEnd");
        }

        Sort sortSpec = "VIEWS".equalsIgnoreCase(sort) ? SORT_BY_VIEWS_DESC : SORT_BY_DATE_DESC;
        Pageable page = PageRequest.of(from / size, size, sortSpec);

        String pattern = (text == null || text.isBlank())
                ? null
                : "%" + text.toLowerCase() + "%";

        Page<Event> result = eventRepo.searchPublished(
                pattern,
                (categories == null || categories.isEmpty()) ? null : categories,
                paid,
                start,
                end,
                page);

        List<Event> events = result.getContent();

        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getParticipantLimit() == 0 ||
                                 e.getConfirmedRequestsCount() < e.getParticipantLimit())
                    .toList();
        }

        setViews(events);
        safeHit(req);

        return mapper.toShortDtoList(events);
    }

    @Override
    public EventFullDto getPublishedEvent(Long id, HttpServletRequest req) {
        Event event = eventRepo.findByIdAndState(id, EventState.PUBLISHED)
                .orElseThrow(() -> new NotFoundException("Event not found or not published"));

        setViews(List.of(event));
        safeHit(req);

        return mapper.toFullDto(event);
    }

    @Transactional
    @Override
    public EventFullDto create(Long userId, NewEventDto dto) {
        User initiator = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("user"));

        Category category = categoryRepo.findById(dto.getCategory())
                .orElseThrow(() -> new NotFoundException("category"));

        Event event = mapper.fromDto(dto);
        event.setInitiator(initiator);
        event.setCategory(category);

        return mapper.toFullDto(eventRepo.saveAndFlush(event));
    }

    @Transactional
    @Override
    public EventFullDto updateByUser(Long userId, Long eventId,
                                     UpdateEventUserRequest dto) {

        Event event = getEventOwned(userId, eventId);

        if (event.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Опубликованное событие нельзя изменить инициатору");
        }

        mapper.apply(dto, event);

        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {

                case SEND_TO_REVIEW -> {
                    if (event.getState() != EventState.CANCELED &&
                        event.getState() != EventState.PENDING)
                        throw new ConflictException("Нельзя отправить событие на модерацию из состояния " + event.getState());

                    event.setState(EventState.PENDING);
                }

                case CANCEL_REVIEW -> {
                    if (event.getState() != EventState.PENDING)
                        throw new ConflictException("Отменить модерацию можно только из состояния PENDING");

                    event.setState(EventState.CANCELED);
                }

                default -> throw new ConflictException("Неподдерживаемое действие");
            }
        }

        return mapper.toFullDto(eventRepo.save(event));
    }

    @Transactional
    @Override
    public EventFullDto cancelByUser(Long userId, Long eventId) {
        Event ev = getEventForInitiator(userId, eventId);

        if (ev.getState() != EventState.PENDING) {
            throw new ConflictException("Only PENDING events can be canceled");
        }
        ev.setState(EventState.CANCELED);
        return mapper.toFullDto(eventRepo.saveAndFlush(ev));
    }

    @Override
    public List<EventShortDto> getUserEvents(Long userId, int from, int size) {
        size = normaliseSize(size);  from = Math.max(from, 0);

        Pageable page = PageRequest.of(from / size, size, Sort.by("id"));
        List<Event> list = eventRepo.findAllByInitiatorId(userId, page).getContent();
        setViews(list);
        return mapper.toShortDtoList(list);
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        Event ev = getEventForInitiator(userId, eventId);
        setViews(List.of(ev));
        return mapper.toFullDto(ev);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EventFullDto> searchAdmin(List<Long> users,
                                          List<String> states,
                                          List<Long> categories,
                                          LocalDateTime rangeStart,
                                          LocalDateTime rangeEnd,
                                          int from, int size) {

        size = normaliseSize(size);
        from = Math.max(from, 0);

        LocalDateTime start = rangeStart != null ? rangeStart : FAR_PAST;
        LocalDateTime end   = rangeEnd   != null ? rangeEnd   : FAR_FUTURE;
        if (start.isAfter(end))
            throw new ConflictException("rangeStart must be before rangeEnd");

        List<EventState> stateEnums = states == null || states.isEmpty()
                ? null
                : states.stream().map(EventState::valueOf).toList();

        Pageable page = PageRequest.of(from / size, size, Sort.by("id"));
        List<Event> events = eventRepo.searchAdmin(
                listOrNull(users),
                stateEnums,
                listOrNull(categories),
                start, end,
                page
        ).getContent();

        Map<Long, Integer> confirmedMap = participationRequestRepo
                .countConfirmedByEventIds(
                        events.stream().map(Event::getId).toList()
                )
                .stream()
                .collect(Collectors.toMap(
                        r -> (Long) r[0],
                        r -> ((Number) r[1]).intValue()
                ));

        setViews(events);
        List<EventFullDto> dtos = mapper.toFullDtoList(events);

        dtos.forEach(d ->
                d.setConfirmedRequests(
                        confirmedMap.getOrDefault(d.getId(), 0)
                )
        );

        return dtos;
    }

    @Transactional
    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest dto) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event"));

        mapper.apply(dto, event);

        if (dto.getCategory() != null) {
            Category c = categoryRepo.findById(dto.getCategory())
                    .orElseThrow(() -> new NotFoundException("category"));
            event.setCategory(c);
        }

        if (dto.getStateAction() != null) {
            switch (dto.getStateAction()) {
                case PUBLISH_EVENT -> {
                    if (event.getState() != EventState.PENDING)
                        throw new ConflictException("Event not PENDING");
                    event.setState(EventState.PUBLISHED);
                    event.setPublishedOn(LocalDateTime.now());
                }
                case REJECT_EVENT -> {
                    if (event.getState() == EventState.PUBLISHED)
                        throw new ConflictException("Event already PUBLISHED");
                    event.setState(EventState.CANCELED);
                }
            }
        }
        return mapper.toFullDto(eventRepo.saveAndFlush(event));
    }

    @Transactional
    @Override
    public EventFullDto publish(Long eventId) {
        Event ev = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event"));

        if (ev.getState() != EventState.PENDING) {
            throw new ConflictException("Event not pending");
        }

        ev.setState(EventState.PUBLISHED);
        ev.setPublishedOn(LocalDateTime.now());

        return mapper.toFullDto(eventRepo.saveAndFlush(ev));
    }

    @Transactional
    @Override
    public EventFullDto reject(Long eventId) {
        Event ev = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event"));

        if (ev.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Cannot reject published event");
        }

        ev.setState(EventState.CANCELED);
        return mapper.toFullDto(eventRepo.saveAndFlush(ev));
    }

    private Event getEventForInitiator(Long userId, Long eventId) {
        Event ev = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event"));
        if (!ev.getInitiator().getId().equals(userId)) {
            throw new ForbiddenException("Not initiator");
        }
        return ev;
    }

    private void setViews(List<Event> events) {
        if (events.isEmpty()) return;

        List<String> uris = events.stream()
                .map(e -> "/events/" + e.getId())
                .toList();

        LocalDateTime start = events.stream()
                .map(Event::getCreatedOn)
                .min(LocalDateTime::compareTo)
                .orElse(LocalDateTime.now().minusYears(1));

        List<ViewStats> statsList;
        try {
            statsList = stats.getStats(start, LocalDateTime.now(), uris, true);
        } catch (Exception ex) {                      // stats-service может быть недоступен
            log.warn("Cannot obtain view stats: {}", ex.getMessage());
            return;
        }

        Map<String, Long> uriViews = statsList.stream()
                .collect(Collectors.toMap(ViewStats::getUri, ViewStats::getHits));

        events.forEach(e -> e.setViews(uriViews.getOrDefault("/events/" + e.getId(), 0L)));
    }

    private Event getEventOwned(Long ownerId, Long eventId) {

        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event"));

        if (!event.getInitiator().getId().equals(ownerId)) {
            throw new ForbiddenException("Current user is not the event owner");
        }

        return event;
    }

    private void safeHit(HttpServletRequest req) {
        try {
            stats.postHit(EndpointHit.of(req, appName));
        } catch (Exception ex) {
            log.warn("Stats service unavailable: {}", ex.getMessage());
        }
    }

    private static int normaliseSize(int size) {
        return size <= 0 ? DEFAULT_SIZE : size;
    }

    private static <T> List<T> listOrNull(List<T> list) {
        return (list == null || list.isEmpty()) ? null : list;
    }
}