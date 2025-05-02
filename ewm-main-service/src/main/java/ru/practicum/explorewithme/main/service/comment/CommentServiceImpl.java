package ru.practicum.explorewithme.main.service.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithme.main.dto.comment.CommentDto;
import ru.practicum.explorewithme.main.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.main.exception.ConflictException;
import ru.practicum.explorewithme.main.exception.NotFoundException;
import ru.practicum.explorewithme.main.mapper.CommentMapper;
import ru.practicum.explorewithme.main.model.event.Event;
import ru.practicum.explorewithme.main.model.event.EventState;
import ru.practicum.explorewithme.main.model.user.User;
import ru.practicum.explorewithme.main.model.comment.Comment;
import ru.practicum.explorewithme.main.model.comment.CommentStatus;
import ru.practicum.explorewithme.main.repository.CommentRepository;
import ru.practicum.explorewithme.main.repository.EventRepository;
import ru.practicum.explorewithme.main.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepo;
    private final EventRepository   eventRepo;
    private final UserRepository    userRepo;
    private final CommentMapper     mapper;

    @Override
    @Transactional
    public CommentDto addComment(Long userId, Long eventId, NewCommentDto dto) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new NotFoundException("event"));
        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Нельзя комментировать неопубликованное событие");
        }

        User author = userRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("user"));

        Comment comment = Comment.builder()
                .event(event)
                .author(author)
                .text(dto.getText())
                .createdOn(LocalDateTime.now())
                .status(CommentStatus.PENDING)
                .build();

        Comment saved = commentRepo.save(comment);
        return mapper.toDto(saved);
    }

    @Override
    public List<CommentDto> getCommentsByEvent(Long eventId, int from, int size) {
        Pageable page = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        return commentRepo
                .findAllByEventIdAndStatus(eventId, CommentStatus.APPROVED, page)
                .map(mapper::toDto)
                .getContent();
    }

    @Override
    @Transactional
    public CommentDto moderateComment(Long commentId, CommentStatus newStatus) {
        Comment comment = commentRepo.findById(commentId)
                .orElseThrow(() -> new NotFoundException("comment"));

        if (comment.getStatus() != CommentStatus.PENDING) {
            throw new ConflictException("Комментарий уже модерирован");
        }
        if (newStatus == CommentStatus.PENDING) {
            throw new ConflictException("Нельзя вернуть комментарий в PENDING");
        }

        comment.setStatus(newStatus);
        return mapper.toDto(commentRepo.save(comment));
    }
}