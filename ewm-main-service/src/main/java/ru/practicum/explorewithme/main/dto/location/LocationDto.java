package ru.practicum.explorewithme.main.dto.location;

import lombok.*;
import jakarta.validation.constraints.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {
    @NotNull
    @DecimalMin("-90.0") @DecimalMax("90.0")
    private Double lat;

    @NotNull
    @DecimalMin("-180.0") @DecimalMax("180.0")
    private Double lon;
}