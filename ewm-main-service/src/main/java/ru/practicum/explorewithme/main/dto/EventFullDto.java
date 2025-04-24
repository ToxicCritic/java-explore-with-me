package ru.practicum.explorewithme.main.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {
    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String annotation;

    @NotBlank
    private String description;

    @NotNull
    private CategoryDto category;

    @NotNull
    private Boolean paid;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;

    @NotNull
    private UserDto initiator;

    @NotNull
    private LocationDto location;

    @NotNull
    @Min(0)
    private Integer participantLimit;

    @NotNull
    private Boolean requestModeration;

    @NotNull
    @Min(0)
    private Integer confirmedRequests;

    @NotNull
    @Min(0)
    private Long views;
}