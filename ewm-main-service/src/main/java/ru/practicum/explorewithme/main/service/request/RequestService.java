package ru.practicum.explorewithme.main.service.request;

import ru.practicum.explorewithme.main.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.main.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.main.dto.request.UpdateEventRequestStatusRequest;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> findForRequester(Long userId);

    ParticipationRequestDto add(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> findForEventOwner(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateStatus(Long userId,
                                                Long eventId,
                                                UpdateEventRequestStatusRequest body);

    ParticipationRequestDto confirm(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto reject(Long userId, Long eventId, Long reqId);
}