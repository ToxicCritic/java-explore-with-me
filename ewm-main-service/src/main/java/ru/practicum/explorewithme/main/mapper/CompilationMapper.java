package ru.practicum.explorewithme.main.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.dto.*;
import ru.practicum.explorewithme.main.model.Compilation;
import ru.practicum.explorewithme.main.model.Event;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;


    public CompilationDto toDto(Compilation c) {
        return CompilationDto.builder()
                .id(c.getId())
                .title(c.getTitle())
                .pinned(c.getPinned())
                .events(eventMapper.toShortDtoList(c.getEvents()))
                .build();
    }

    public Compilation fromDto(NewCompilationDto dto, List<Event> events) {
        return Compilation.builder()
                .title(dto.getTitle())
                .pinned(dto.getPinned() != null && dto.getPinned())
                .events(events)
                .build();
    }

    public void updateFromDto(UpdateCompilationRequest dto,
                              Compilation target,
                              Function<Iterable<Long>, List<Event>> eventsFetcher) {

        if (dto.getTitle() != null)   target.setTitle(dto.getTitle());
        if (dto.getPinned() != null)  target.setPinned(dto.getPinned());

        if (dto.getEvents() != null) {
            List<Event> newEvents = eventsFetcher.apply(dto.getEvents());
            target.setEvents(newEvents);
        }
    }

    public List<CompilationDto> toDto(List<Compilation> list) {
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }
}