package ru.practicum.explorewithme.main.repository;

import org.springframework.data.jpa.repository.*;
import ru.practicum.explorewithme.main.model.ParticipationRequest;
import ru.practicum.explorewithme.main.model.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface ParticipationRequestRepository
        extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByRequesterId(Long requesterId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    Optional<ParticipationRequest> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    long countByEventIdAndStatus(Long eventId, RequestStatus status);

    boolean existsByRequesterIdAndEventId(Long requesterId, Long eventId);

    Optional<ParticipationRequest> findByIdAndRequesterId(Long id, Long requesterId);

    @Modifying
    @Query("""
           UPDATE ParticipationRequest pr
           SET pr.status = :status
           WHERE pr.id IN :ids
           """)
    int updateStatusBatch(List<Long> ids, RequestStatus status);
}