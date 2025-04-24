package ru.practicum.explorewithme.main.model;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false)
    private String title;

    @NotBlank
    @Column(nullable = false)
    private String annotation;

    @NotBlank
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @NotNull
    @Column(nullable = false)
    private Boolean paid;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @NotNull
    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Embedded
    private Location location;

    @NotNull
    @Min(0)
    @Column(name = "participant_limit", nullable = false)
    private Integer participantLimit;

    @NotNull
    @Column(name = "request_moderation", nullable = false)
    private Boolean requestModeration;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventState state;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipationRequest> requests = new ArrayList<>();

    @Transient
    private Long views;

    public int getConfirmedRequestsCount() {
        return (int) requests.stream()
                .filter(r -> r.getStatus() == RequestStatus.CONFIRMED)
                .count();
    }
}