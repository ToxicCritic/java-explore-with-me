package ru.practicum.explorewithme.main.service.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.explorewithme.main.dto.event.*;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    List<EventShortDto> searchPublished(String text, List<Long> categories, Boolean paid,
                                        LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                        boolean onlyAvailable, String sort, int from, int size,
                                        HttpServletRequest request);

    EventFullDto getPublishedEvent(Long id, HttpServletRequest request);

    EventFullDto create(Long userId, NewEventDto dto);

    EventFullDto updateByUser(Long userId, Long eventId, UpdateEventUserRequest dto);

    List<EventShortDto> getUserEvents(Long userId, int from, int size);

    EventFullDto getUserEvent(Long userId, Long eventId);

    EventFullDto cancelByUser(Long userId, Long eventId);

    List<EventFullDto> searchAdmin(List<Long> users, List<String> states, List<Long> categories,
                                   LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                   int from, int size);

    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequest dto);

    EventFullDto publish(Long eventId);

    EventFullDto reject(Long eventId);
}