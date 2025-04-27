package ru.practicum.explorewithme.main.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.dto.category.CategoryDto;
import ru.practicum.explorewithme.main.dto.category.NewCategoryDto;
import ru.practicum.explorewithme.main.model.category.Category;

@Component
public class CategoryMapper {
    public CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public Category fromDto(NewCategoryDto dto) {
        if (dto == null) return null;
        return Category.builder()
                .name(dto.getName())
                .build();
    }
}
