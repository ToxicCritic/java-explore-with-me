package ru.practicum.explorewithme.main.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.main.model.request.RequestStatus;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventRequestStatusRequest {
    @NotEmpty
    private List<Long> requestIds;

    @NotNull
    private RequestStatus status;
}

