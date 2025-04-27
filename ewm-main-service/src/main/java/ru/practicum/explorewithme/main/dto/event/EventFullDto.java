package ru.practicum.explorewithme.main.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import jakarta.validation.constraints.*;
import ru.practicum.explorewithme.main.dto.location.LocationDto;
import ru.practicum.explorewithme.main.dto.category.CategoryDto;
import ru.practicum.explorewithme.main.dto.user.UserDto;
import ru.practicum.explorewithme.main.model.event.EventState;

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

    private Boolean paid;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private LocalDateTime createdOn;

    private LocalDateTime publishedOn;

    @NotNull
    private UserDto initiator;

    @NotNull
    private LocationDto location;

    @Min(0)
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotNull
    private EventState state;

    @NotNull
    @Min(0)
    private Integer confirmedRequests;

    @NotNull
    @Min(0)
    private Long views;
}