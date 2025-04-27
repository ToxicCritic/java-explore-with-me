package ru.practicum.explorewithme.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.main.model.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
}