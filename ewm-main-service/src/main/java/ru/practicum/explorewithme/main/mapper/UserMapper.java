package ru.practicum.explorewithme.main.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.dto.NewUserRequest;
import ru.practicum.explorewithme.main.dto.UserDto;
import ru.practicum.explorewithme.main.model.User;

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

    public User fromDto(NewUserRequest dto) {
        if (dto == null) return null;

        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}
