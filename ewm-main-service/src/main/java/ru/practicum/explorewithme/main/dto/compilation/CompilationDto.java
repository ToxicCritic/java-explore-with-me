package ru.practicum.explorewithme.main.dto.compilation;

import lombok.*;
import jakarta.validation.constraints.*;
import ru.practicum.explorewithme.main.dto.event.EventShortDto;

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