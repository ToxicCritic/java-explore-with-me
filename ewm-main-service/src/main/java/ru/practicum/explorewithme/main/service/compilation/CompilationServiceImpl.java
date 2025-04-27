package ru.practicum.explorewithme.main.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.main.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.main.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.mapper.CompilationMapper;
import ru.practicum.explorewithme.main.model.compilation.Compilation;
import ru.practicum.explorewithme.main.model.event.Event;
import ru.practicum.explorewithme.main.repository.CompilationRepository;
import ru.practicum.explorewithme.main.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository repo;
    private final EventRepository eventRepo;
    private final CompilationMapper mapper;

    @Override
    public List<CompilationDto> findAll(Boolean pinned, int from, int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("id"));

        Page<Compilation> comps = (pinned == null)
                ? repo.findAll(page)
                : repo.findByPinned(pinned, page);

        return mapper.toDto(comps.getContent());
    }

    @Override
    public CompilationDto find(Long id) {
        Compilation cmp = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
        return mapper.toDto(cmp);
    }

    @Transactional
    @Override
    public CompilationDto add(NewCompilationDto dto) {
        List<Long> ids = dto.getEvents();
        List<Event> events = (ids == null || ids.isEmpty())
                ? List.of()
                : eventRepo.findAllById(ids);

        Compilation cmp = mapper.fromDto(dto, events);
        cmp = repo.saveAndFlush(cmp);
        return mapper.toDto(cmp);
    }

    @Transactional
    @Override
    public CompilationDto edit(Long id, UpdateCompilationRequest dto) {
        Compilation cmp = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
        mapper.updateFromDto(
                dto,
                cmp,
                ids -> ids == null
                        ? List.of()
                        : eventRepo.findAllById(ids)
        );
        return mapper.toDto(cmp);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}