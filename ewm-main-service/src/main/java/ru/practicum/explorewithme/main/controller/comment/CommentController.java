package ru.practicum.explorewithme.main.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithme.main.dto.comment.CommentDto;
import ru.practicum.explorewithme.main.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.main.model.comment.CommentStatus;
import ru.practicum.explorewithme.main.service.comment.CommentService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping
@Validated
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @Valid @RequestBody NewCommentDto dto
    ) {
        return commentService.addComment(userId, eventId, dto);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getCommentsByEvent(
            @PathVariable Long eventId,
            @RequestParam(defaultValue = "0") @Min(0) int from,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        return commentService.getCommentsByEvent(eventId, from, size);
    }

    @PatchMapping("/admin/comments/{commentId}")
    public CommentDto moderateComment(
            @PathVariable Long commentId,
            @RequestParam CommentStatus status
    ) {
        return commentService.moderateComment(commentId, status);
    }
}