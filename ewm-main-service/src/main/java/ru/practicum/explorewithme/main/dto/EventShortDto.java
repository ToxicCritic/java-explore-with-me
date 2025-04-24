package ru.practicum.explorewithme.main.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    @NotNull
    private Boolean paid;

    @NotNull
    private LocalDateTime eventDate;

    @NotNull
    @Min(0)
    private Integer confirmedRequests;

    @NotNull
    @Min(0)
    private Long views;
}