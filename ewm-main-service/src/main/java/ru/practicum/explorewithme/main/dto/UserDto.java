package ru.practicum.explorewithme.main.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    private String email;
}