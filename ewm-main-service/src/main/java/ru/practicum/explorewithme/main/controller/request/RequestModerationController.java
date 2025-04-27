package ru.practicum.explorewithme.main.controller.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.dto.request.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.main.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.main.dto.request.UpdateEventRequestStatusRequest;
import ru.practicum.explorewithme.main.service.request.RequestService;

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