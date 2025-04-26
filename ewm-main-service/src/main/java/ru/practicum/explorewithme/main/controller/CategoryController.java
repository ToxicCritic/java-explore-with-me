package ru.practicum.explorewithme.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.dto.CategoryDto;
import ru.practicum.explorewithme.main.dto.NewCategoryDto;
import ru.practicum.explorewithme.main.service.CategoryService;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    @GetMapping("/categories")
    public List<CategoryDto> findAll(@RequestParam(defaultValue = "0") @Min(0) int from,
                                     @RequestParam(defaultValue = "10") @Min(1) int size) {
        return service.findAll(from, size);
    }

    @GetMapping("/categories/{id}")
    public CategoryDto find(@PathVariable Long id) { return service.find(id); }

    @PostMapping("/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto add(@RequestBody @Valid NewCategoryDto dto) { return service.add(dto); }

    @PatchMapping("/admin/categories/{id}")
    public CategoryDto edit(@PathVariable Long id, @RequestBody @Valid NewCategoryDto dto) {
        return service.edit(id, dto);
    }

    @DeleteMapping("/admin/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) { service.delete(id); }
}