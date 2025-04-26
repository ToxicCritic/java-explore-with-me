package ru.practicum.explorewithme.main.service;

import ru.practicum.explorewithme.main.dto.NewUserRequest;
import ru.practicum.explorewithme.main.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> find(List<Long> ids, int from, int size);

    UserDto add(NewUserRequest dto);

    void delete(Long id);
}