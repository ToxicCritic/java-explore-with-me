package ru.practicum.explorewithme.stats.service.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.stats.common.dto.EndpointHit;
import ru.practicum.explorewithme.stats.common.dto.ViewStats;
import ru.practicum.explorewithme.stats.service.service.StatsService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Validated
@RestController
public class StatsController {

    private final StatsService service;
    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public StatsController(StatsService service) {
        this.service = service;
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void hit(@Valid @RequestBody EndpointHit hit) {
        service.addHit(hit);
    }

    @GetMapping("/stats")
    public List<ViewStats> stats(
            @RequestParam String start,
            @RequestParam String end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(defaultValue = "false") boolean unique
    ) {
        LocalDateTime sd = LocalDateTime.parse(
                URLDecoder.decode(start, StandardCharsets.UTF_8), FMT);
        LocalDateTime ed = LocalDateTime.parse(
                URLDecoder.decode(end, StandardCharsets.UTF_8), FMT);
        return service.getStats(sd, ed, uris, unique);
    }
}