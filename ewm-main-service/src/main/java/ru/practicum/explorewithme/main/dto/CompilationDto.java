package ru.practicum.explorewithme.main.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompilationDto {
    @NotNull
    private Long id;

    @NotBlank
    private String title;

    private Boolean pinned;

    @NotNull
    @Size(min = 0)
    private List<@NotNull EventShortDto> events;
}