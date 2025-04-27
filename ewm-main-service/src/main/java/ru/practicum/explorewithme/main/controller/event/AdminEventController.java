package ru.practicum.explorewithme.main.controller.event;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.dto.event.EventFullDto;
import ru.practicum.explorewithme.main.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.main.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> search(@RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<String> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(required = false)
                                                     // вот бы форматтер написать на это все
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                     LocalDateTime rangeStart,
                                                     @RequestParam(required = false)
                                                     @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                     LocalDateTime rangeEnd,
                                                     @RequestParam(defaultValue = "0")  @Min(0) Integer from,
                                                     @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        List<EventFullDto> result = eventService.searchAdmin(
                users, states, categories, rangeStart, rangeEnd, from, size);

        return ResponseEntity.ok(result);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto update(@PathVariable Long eventId,
                               @RequestBody UpdateEventAdminRequest dto) {
        return eventService.updateByAdmin(eventId, dto);
    }

    @PatchMapping("/{eventId}/publish")
    public EventFullDto publish(@PathVariable Long eventId) {
        return eventService.publish(eventId);
    }

    @PatchMapping("/{eventId}/reject")
    public EventFullDto reject(@PathVariable Long eventId) {
        return eventService.reject(eventId);
    }
}