package ru.practicum.explorewithme.stats.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.stats.common.dto.ViewStats;
import ru.practicum.explorewithme.stats.service.entity.EndpointHitEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitRepository extends JpaRepository<EndpointHitEntity, Long> {

    @Query("SELECT new ru.practicum.explorewithme.stats.common.dto.ViewStats(e.app, e.uri, COUNT(e)) " +
           "FROM EndpointHitEntity e " +
           "WHERE e.timestamp BETWEEN :start AND :end " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT(e) DESC")
    List<ViewStats> findStatsAll(@Param("start") LocalDateTime start,
                                 @Param("end")   LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.stats.common.dto.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
           "FROM EndpointHitEntity e " +
           "WHERE e.timestamp BETWEEN :start AND :end " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findUniqueStatsAll(@Param("start") LocalDateTime start,
                                       @Param("end")   LocalDateTime end);

    @Query("SELECT new ru.practicum.explorewithme.stats.common.dto.ViewStats(e.app, e.uri, COUNT(e)) " +
           "FROM EndpointHitEntity e " +
           "WHERE e.timestamp BETWEEN :start AND :end " +
           "AND e.uri IN :uris " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT(e) DESC")
    List<ViewStats> findStats(@Param("start") LocalDateTime start,
                              @Param("end")   LocalDateTime end,
                              @Param("uris")  List<String> uris);

    @Query("SELECT new ru.practicum.explorewithme.stats.common.dto.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
           "FROM EndpointHitEntity e " +
           "WHERE e.timestamp BETWEEN :start AND :end " +
           "AND e.uri IN :uris " +
           "GROUP BY e.app, e.uri " +
           "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> findUniqueStats(@Param("start") LocalDateTime start,
                                    @Param("end")   LocalDateTime end,
                                    @Param("uris")  List<String> uris);
}