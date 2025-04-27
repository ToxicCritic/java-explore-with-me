package ru.practicum.explorewithme.main.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.dto.NewUserRequest;
import ru.practicum.explorewithme.main.dto.UserDto;
import ru.practicum.explorewithme.main.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {
    private final UserService service;

    @GetMapping
    public List<UserDto> search(@RequestParam(required = false) List<Long> ids,
                                @RequestParam(defaultValue = "0")  @Min(0) int from,
                                @RequestParam(defaultValue = "10") @Min(1) int size) {
        return service.find(ids, from, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto add(@Valid @RequestBody NewUserRequest dto) {
        return service.add(dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}