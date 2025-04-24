package ru.practicum.explorewithme.main.service;

import ru.practicum.explorewithme.main.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> findForRequester(Long userId);

    ParticipationRequestDto add(Long userId, Long eventId);

    ParticipationRequestDto cancel(Long userId, Long requestId);

    List<ParticipationRequestDto> findForEventOwner(Long userId, Long eventId);

    ParticipationRequestDto confirm(Long userId, Long eventId, Long reqId);

    ParticipationRequestDto reject(Long userId, Long eventId, Long reqId);
}