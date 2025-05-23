package ru.practicum.explorewithme.main.dto.compilation;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String title;

    private List<Long> events;

    private Boolean pinned;
}