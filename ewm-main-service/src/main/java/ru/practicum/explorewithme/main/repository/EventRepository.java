package ru.practicum.explorewithme.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.explorewithme.main.model.Event;
import ru.practicum.explorewithme.main.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("""
       SELECT e
       FROM Event e
       WHERE e.state = 'PUBLISHED'
         AND ( :text IS NULL OR
               LOWER(e.annotation)  LIKE :text OR
               LOWER(e.description) LIKE :text )
         AND ( :paid   IS NULL OR e.paid          = :paid   )
         AND ( :catIds IS NULL OR e.category.id  IN :catIds )
         AND e.eventDate BETWEEN :start AND :end
       """)
    Page<Event> searchPublished(@Param("text") String text,
                                @Param("catIds") List<Long> catIds,
                                @Param("paid")   Boolean paid,
                                @Param("start") LocalDateTime start,
                                @Param("end")    LocalDateTime end,
                                Pageable page);

    Optional<Event> findByIdAndState(Long id, EventState state);

    boolean existsByCategoryId(Long categoryId);

    Page<Event> findAllByInitiatorId(Long userId, Pageable page);

    @Query("""
           SELECT e
           FROM Event e
           WHERE (:users IS NULL OR e.initiator.id IN :users)
             AND (:states IS NULL OR e.state IN :states)
             AND (:catIds IS NULL OR e.category.id IN :catIds)
             AND e.eventDate BETWEEN :start AND :end
           """)
    Page<Event> searchAdmin(@Param("users")  List<Long> users,
                            @Param("states") List<EventState> states,
                            @Param("catIds") List<Long> catIds,
                            @Param("start")  LocalDateTime start,
                            @Param("end")    LocalDateTime end,
                            Pageable page);
}