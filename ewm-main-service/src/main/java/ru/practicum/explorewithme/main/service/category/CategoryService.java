package ru.practicum.explorewithme.main.service.category;

import ru.practicum.explorewithme.main.dto.category.CategoryDto;
import ru.practicum.explorewithme.main.dto.category.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto add(NewCategoryDto dto);

    CategoryDto edit(Long id, NewCategoryDto dto);

    void delete(Long id);

    List<CategoryDto> findAll(int from, int size);

    CategoryDto find(Long id);
}