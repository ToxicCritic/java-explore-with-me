package ru.practicum.explorewithme.main.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.dto.event.EventFullDto;
import ru.practicum.explorewithme.main.dto.event.EventShortDto;
import ru.practicum.explorewithme.main.dto.event.NewEventDto;
import ru.practicum.explorewithme.main.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.main.service.event.EventService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {

    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto add(@PathVariable Long userId,
                            @Valid @RequestBody NewEventDto dto) {
        return eventService.create(userId, dto);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long userId,
                               @PathVariable Long eventId,
                               @Valid @RequestBody UpdateEventUserRequest dto) {
        return eventService.updateByUser(userId, eventId, dto);
    }

    @GetMapping
    public List<EventShortDto> getUserEvents(@PathVariable Long userId,
                                             @RequestParam(defaultValue = "0")  @Min(0) Integer from,
                                             @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        return eventService.getUserEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getOwn(@PathVariable Long userId,
                               @PathVariable Long eventId) {
        return eventService.getUserEvent(userId, eventId);
    }

    @PatchMapping("/{eventId}/cancel")
    public EventFullDto cancel(@PathVariable Long userId,
                               @PathVariable Long eventId) {
        return eventService.cancelByUser(userId, eventId);
    }
}