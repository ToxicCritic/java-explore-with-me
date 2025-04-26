package ru.practicum.explorewithme.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.dto.*;
import ru.practicum.explorewithme.main.exception.*;
import ru.practicum.explorewithme.main.mapper.UserMapper;
import ru.practicum.explorewithme.main.model.User;
import ru.practicum.explorewithme.main.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final int DEFAULT_SIZE = 10;

    private final UserRepository repo;
    private final UserMapper     mapper;

    @Override
    public List<UserDto> find(List<Long> ids, int from, int size) {

        size = size <= 0 ? DEFAULT_SIZE : size;
        from = Math.max(from, 0);

        if (ids == null || ids.isEmpty()) {
            return repo.findAll(PageRequest.of(from / size, size))
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
        return repo.findAllById(ids)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public UserDto add(NewUserRequest dto) {
        if (repo.existsByEmail(dto.getEmail())) {
            throw new ConflictException("E-mail already in use");
        }
        User saved = repo.save(mapper.fromDto(dto));
        return mapper.toDto(saved);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("user");
        }
        repo.deleteById(id);
    }
}