package ru.practicum.explorewithme.main.service.comment;

import ru.practicum.explorewithme.main.dto.comment.CommentDto;
import ru.practicum.explorewithme.main.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.main.model.comment.CommentStatus;

import java.util.List;

public interface CommentService {
    CommentDto addComment(Long userId, Long eventId, NewCommentDto dto);

    List<CommentDto> getCommentsByEvent(Long eventId, int from, int size);

    CommentDto moderateComment(Long commentId, CommentStatus newStatus);
}