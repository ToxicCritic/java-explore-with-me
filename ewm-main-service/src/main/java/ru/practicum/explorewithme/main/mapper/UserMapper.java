package ru.practicum.explorewithme.main.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.dto.user.NewUserDto;
import ru.practicum.explorewithme.main.dto.user.UserDto;
import ru.practicum.explorewithme.main.model.user.User;

@Component
public class UserMapper {

    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public User fromDto(NewUserDto dto) {
        if (dto == null) return null;

        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}
