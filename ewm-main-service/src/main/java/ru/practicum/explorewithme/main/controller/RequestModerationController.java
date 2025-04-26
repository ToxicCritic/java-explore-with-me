package ru.practicum.explorewithme.main.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.dto.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.main.dto.ParticipationRequestDto;
import ru.practicum.explorewithme.main.dto.UpdateEventRequestStatusRequest;
import ru.practicum.explorewithme.main.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}/requests")
public class RequestModerationController {
    private final RequestService service;

    @GetMapping
    public List<ParticipationRequestDto> find(@PathVariable Long userId,
                                              @PathVariable Long eventId) {
        return service.findForEventOwner(userId, eventId);
    }

    @PatchMapping
    public EventRequestStatusUpdateResult updateStatus(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody UpdateEventRequestStatusRequest body) {
        return service.updateStatus(userId, eventId, body);
    }

    @PatchMapping("/{reqId}/confirm")
    public ParticipationRequestDto confirm(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @PathVariable Long reqId) {
        return service.confirm(userId, eventId, reqId);
    }

    @PatchMapping("/{reqId}/reject")
    public ParticipationRequestDto reject(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @PathVariable Long reqId) {
        return service.reject(userId, eventId, reqId);
    }
}