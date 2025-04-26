package ru.practicum.explorewithme.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.dto.*;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.mapper.CompilationMapper;
import ru.practicum.explorewithme.main.model.Compilation;
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
        Compilation cmp = mapper.fromDto(dto, eventRepo.findAllById(dto.getEvents()));
        return mapper.toDto(repo.save(cmp));
    }

    @Transactional
    @Override
    public CompilationDto edit(Long id, UpdateCompilationRequest dto) {
        Compilation cmp = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("Compilation not found"));
        mapper.updateFromDto(dto, cmp, eventRepo::findAllById);
        return mapper.toDto(cmp);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}