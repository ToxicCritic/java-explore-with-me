package ru.practicum.explorewithme.main.service.compilation;

import ru.practicum.explorewithme.main.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.main.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.main.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> findAll(Boolean pinned, int from, int size);

    CompilationDto find(Long id);

    CompilationDto add(NewCompilationDto dto);

    CompilationDto edit(Long id, UpdateCompilationRequest dto);

    void delete(Long id);
}