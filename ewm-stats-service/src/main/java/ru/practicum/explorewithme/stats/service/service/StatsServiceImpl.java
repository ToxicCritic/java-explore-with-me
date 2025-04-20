package ru.practicum.explorewithme.stats.service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.stats.common.dto.EndpointHit;
import ru.practicum.explorewithme.stats.common.dto.ViewStats;
import ru.practicum.explorewithme.stats.service.entity.EndpointHitEntity;
import ru.practicum.explorewithme.stats.service.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {

    private final EndpointHitRepository repository;

    @Override
    @Transactional
    public void addHit(EndpointHit dto) {
        EndpointHitEntity e = new EndpointHitEntity();
        e.setApp(dto.getApp());
        e.setUri(dto.getUri());
        e.setIp(dto.getIp());
        e.setTimestamp(dto.getTimestamp());
        repository.save(e);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewStats> getStats(LocalDateTime start,
                                    LocalDateTime end,
                                    List<String> uris,
                                    boolean unique) {

        boolean noFilter = (uris == null || uris.isEmpty());

        if (noFilter) {
            return unique
                    ? repository.findUniqueStatsAll(start, end)
                    : repository.findStatsAll(start, end);
        } else {
            return unique
                    ? repository.findUniqueStats(start, end, uris)
                    : repository.findStats(start, end, uris);
        }
    }
}