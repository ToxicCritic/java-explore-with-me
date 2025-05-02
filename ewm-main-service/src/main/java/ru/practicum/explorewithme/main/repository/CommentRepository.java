package ru.practicum.explorewithme.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.main.model.comment.Comment;
import ru.practicum.explorewithme.main.model.comment.CommentStatus;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByEventIdAndStatus(Long eventId, CommentStatus status, Pageable pageable);

    Page<Comment> findAllByAuthorId(Long authorId, Pageable pageable);
}