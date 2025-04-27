package ru.practicum.explorewithme.main.dto.user;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    @NotNull
    private Long id;

    @NotBlank
    private String name;
}