package ru.practicum.explorewithme.main.dto.category;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @NotNull
    private Long id;

    @NotBlank
    private String name;
}