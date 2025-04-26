package ru.practicum.explorewithme.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.dto.CategoryDto;
import ru.practicum.explorewithme.main.dto.NewCategoryDto;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.mapper.CategoryMapper;
import ru.practicum.explorewithme.main.model.Category;
import ru.practicum.explorewithme.main.repository.CategoryRepository;
import ru.practicum.explorewithme.main.repository.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository catRepo;
    private final EventRepository eventRepo;
    private final CategoryMapper mapper;

    @Transactional
    @Override
    public CategoryDto add(NewCategoryDto dto) {
        if (catRepo.existsByNameIgnoreCase(dto.getName())) {
            throw new ConflictException("Category name already exists");
        }
        Category saved = catRepo.save(mapper.fromDto(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    @Override
    public CategoryDto edit(Long id, NewCategoryDto dto) {
        Category cat = catRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("category"));

        if (!cat.getName().equals(dto.getName())) {
            if (catRepo.existsByNameIgnoreCase(dto.getName())) {
                throw new ConflictException("Category name already exists");
            }
            cat.setName(dto.getName());
        }
        return mapper.toDto(cat);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Category cat = catRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("category"));

        if (eventRepo.existsByCategoryId(cat.getId()))
            throw new ConflictException("Category is linked to events");

        catRepo.delete(cat);
    }

    @Override
    public List<CategoryDto> findAll(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return catRepo.findAll(page).map(mapper::toDto).getContent();
    }

    @Override
    public CategoryDto find(Long id) {
        return mapper.toDto(catRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("category")));
    }
}