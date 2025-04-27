package ru.practicum.explorewithme.main.model.event;

import lombok.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import ru.practicum.explorewithme.main.model.category.Category;
import ru.practicum.explorewithme.main.model.location.Location;
import ru.practicum.explorewithme.main.model.request.ParticipationRequest;
import ru.practicum.explorewithme.main.model.request.RequestStatus;
import ru.practicum.explorewithme.main.model.user.User;

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
    @Size(min = 3, max = 120)
    @Column(nullable = false, length = 120)
    private String title;

    @NotBlank
    @Size(min = 20, max = 2000)
    @Column(nullable = false, length = 2000)
    private String annotation;

    @NotBlank
    @Size(min = 20, max = 7000)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false, columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean paid = false;

    @NotNull
    @FutureOrPresent
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

    @Min(0)
    @Column(name = "participant_limit",
            nullable = false,
            columnDefinition = "integer default 0")
    @Builder.Default
    private Integer participantLimit = 0;


    @Column(name = "request_moderation",
            nullable = false,
            columnDefinition = "boolean default false")
    @Builder.Default
    private Boolean requestModeration = false;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventState state;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ParticipationRequest> requests = new ArrayList<>();

    @Transient
    private Long views;

    public int getConfirmedRequestsCount() {
        if (requests == null) {
            return 0;
        }
        return (int) requests.stream()
                .filter(r -> !r.getRequester().getId().equals(initiator.getId()))
                .filter(r -> r.getStatus() == RequestStatus.CONFIRMED)
                .count();
    }
}