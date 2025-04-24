package ru.practicum.explorewithme.main.service;

import ru.practicum.explorewithme.main.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto add(CategoryDto dto);
    CategoryDto edit(Long id, CategoryDto dto);
    void delete(Long id);
    List<CategoryDto> findAll(int from, int size);
    CategoryDto find(Long id);
}