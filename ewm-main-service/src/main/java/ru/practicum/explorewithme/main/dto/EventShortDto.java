package ru.practicum.explorewithme.main.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private UserDto initiator;

    @NotNull
    @Min(0)
    private Integer confirmedRequests;

    @NotNull
    @Min(0)
    private Long views;
}