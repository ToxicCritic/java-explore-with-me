package ru.practicum.explorewithme.main.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.explorewithme.main.dto.comment.CommentDto;
import ru.practicum.explorewithme.main.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.main.model.comment.Comment;
import ru.practicum.explorewithme.main.model.comment.CommentStatus;
import ru.practicum.explorewithme.main.model.event.Event;
import ru.practicum.explorewithme.main.model.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final UserMapper userMapper;

    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        return CommentDto.builder()
                .id(comment.getId())
                .eventId(comment.getEvent().getId())
                .authorId(comment.getAuthor().getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .createdOn(comment.getCreatedOn())
                .status(comment.getStatus())
                .build();
    }

    public List<CommentDto> toDtoList(List<Comment> comments) {
        return comments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public Comment fromDto(NewCommentDto dto, User author, Event event) {
        Comment comment = Comment.builder()
                .text(dto.getText())
                .author(author)
                .event(event)
                .createdOn(LocalDateTime.now())
                .status(CommentStatus.PENDING)
                .build();
        return comment;
    }
}
