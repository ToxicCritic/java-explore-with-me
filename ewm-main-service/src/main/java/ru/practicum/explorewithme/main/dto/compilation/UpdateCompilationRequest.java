package ru.practicum.explorewithme.main.dto.compilation;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {
    private String title;

    private List<Long> events;

    private Boolean pinned;
}