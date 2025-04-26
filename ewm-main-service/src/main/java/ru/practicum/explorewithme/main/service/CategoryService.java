package ru.practicum.explorewithme.main.service;

import ru.practicum.explorewithme.main.dto.CategoryDto;
import ru.practicum.explorewithme.main.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto add(NewCategoryDto dto);

    CategoryDto edit(Long id, NewCategoryDto dto);

    void delete(Long id);

    List<CategoryDto> findAll(int from, int size);

    CategoryDto find(Long id);
}