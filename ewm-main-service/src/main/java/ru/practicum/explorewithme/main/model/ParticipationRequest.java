package ru.practicum.explorewithme.main.model;

import jakarta.persistence.Entity;
import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @NotNull
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;
}