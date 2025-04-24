// src/main/java/ru/practicum/explorewithme/main/mapper/EventMapper.java
package ru.practicum.explorewithme.main.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.dto.*;
import ru.practicum.explorewithme.main.model.Event;
import ru.practicum.explorewithme.main.model.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;
    private final UserMapper      userMapper;
    private final LocationMapper  locationMapper;
    public EventShortDto toShortDto(Event e) {
        if (e == null) return null;

        return EventShortDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .annotation(e.getAnnotation())
                .category(categoryMapper.toDto(e.getCategory()))
                .paid(e.getPaid())
                .eventDate(e.getEventDate())
                .confirmedRequests(e.getConfirmedRequestsCount())
                .views(e.getViews() == null ? 0 : e.getViews())
                .build();
    }

    public EventFullDto toFullDto(Event e) {
        if (e == null) return null;

        return EventFullDto.builder()
                .id(e.getId())
                .title(e.getTitle())
                .annotation(e.getAnnotation())
                .description(e.getDescription())
                .category(categoryMapper.toDto(e.getCategory()))
                .paid(e.getPaid())
                .eventDate(e.getEventDate())
                .createdOn(e.getCreatedOn())
                .publishedOn(e.getPublishedOn())
                .initiator(userMapper.toDto(e.getInitiator()))
                .location(locationMapper.toDto(e.getLocation()))
                .participantLimit(e.getParticipantLimit())
                .requestModeration(e.getRequestModeration())
                .confirmedRequests(e.getConfirmedRequestsCount())
                .views(e.getViews() == null ? 0 : e.getViews())
                .build();
    }

    public List<EventShortDto> toShortDtoList(List<Event> list) {
        return list.stream().map(this::toShortDto).collect(Collectors.toList());
    }

    public List<EventFullDto> toFullDtoList(List<Event> list) {
        return list.stream().map(this::toFullDto).collect(Collectors.toList());
    }

    public Event fromDto(NewEventDto dto) {
        return Event.builder()
                .title(dto.getTitle())
                .annotation(dto.getAnnotation())
                .description(dto.getDescription())
                .paid(dto.getPaid())
                .eventDate(dto.getEventDate())
                .createdOn(LocalDateTime.now())
                .participantLimit(
                        dto.getParticipantLimit() == null ? 0 : dto.getParticipantLimit())
                .requestModeration(dto.getRequestModeration())
                .state(EventState.PENDING)
                .location(locationMapper.fromDto(dto.getLocation()))
                .build();
    }

    public void apply(UpdateEventUserRequest dto, Event e) {
        if (dto.getTitle()            != null) e.setTitle(dto.getTitle());
        if (dto.getAnnotation()       != null) e.setAnnotation(dto.getAnnotation());
        if (dto.getDescription()      != null) e.setDescription(dto.getDescription());
        if (dto.getPaid()             != null) e.setPaid(dto.getPaid());
        if (dto.getEventDate()        != null) e.setEventDate(dto.getEventDate());
        if (dto.getParticipantLimit() != null) e.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getRequestModeration()!= null) e.setRequestModeration(dto.getRequestModeration());

        if (dto.getLocation() != null) {
            e.setLocation(locationMapper.fromDto(dto.getLocation()));
        }
    }

    public void apply(UpdateEventAdminRequest dto, Event e) {
        if (dto.getTitle()       != null) e.setTitle(dto.getTitle());
        if (dto.getAnnotation()  != null) e.setAnnotation(dto.getAnnotation());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
        if (dto.getPaid()        != null) e.setPaid(dto.getPaid());
        if (dto.getEventDate()   != null) e.setEventDate(dto.getEventDate());
        if (dto.getParticipantLimit() != null)
            e.setParticipantLimit(dto.getParticipantLimit());
        if (dto.getLocation() != null)
            e.setLocation(locationMapper.fromDto(dto.getLocation()));

    }
}