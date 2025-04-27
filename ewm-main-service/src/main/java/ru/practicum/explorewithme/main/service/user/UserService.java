package ru.practicum.explorewithme.main.service.user;

import ru.practicum.explorewithme.main.dto.user.NewUserDto;
import ru.practicum.explorewithme.main.dto.user.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> find(List<Long> ids, int from, int size);

    UserDto add(NewUserDto dto);

    void delete(Long id);
}