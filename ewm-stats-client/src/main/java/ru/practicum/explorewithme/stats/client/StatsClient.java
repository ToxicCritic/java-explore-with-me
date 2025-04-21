package ru.practicum.explorewithme.stats.client;

import ru.practicum.explorewithme.stats.common.dto.EndpointHit;
import ru.practicum.explorewithme.stats.common.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {

    void postHit(EndpointHit hit);

    List<ViewStats> getStats(LocalDateTime start,
                             LocalDateTime end,
                             List<String> uris,
                             boolean unique);
}