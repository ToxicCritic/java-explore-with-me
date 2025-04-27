package ru.practicum.explorewithme.main.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.main.model.request.ParticipationRequest;
import ru.practicum.explorewithme.main.model.request.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    @Query("""
            SELECT e.id, COUNT(r)
            FROM ParticipationRequest r
            JOIN r.event e
            WHERE r.status = 'CONFIRMED'
              AND e.id IN :eventIds
              AND r.requester.id <> e.initiator.id
            GROUP BY e.id
            """)
    List<Object[]> countConfirmedByEventIds(@Param("eventIds") List<Long> eventIds);

    List<ParticipationRequest> findAllByEventIdAndStatus(Long eventId, RequestStatus requestStatus);
}