package ru.practicum.explorewithme.main.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.dto.request.ParticipationRequestDto;
import ru.practicum.explorewithme.main.model.request.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RequestMapper {

    public ParticipationRequestDto toDto(ParticipationRequest pr) {
        if (pr == null) return null;

        return ParticipationRequestDto.builder()
                .id(pr.getId())
                .created(pr.getCreated())
                .event(pr.getEvent().getId())
                .requester(pr.getRequester().getId())
                .status(pr.getStatus().name())
                .build();
    }

    public List<ParticipationRequestDto> toDto(List<ParticipationRequest> list) {
        return list.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}