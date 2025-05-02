package ru.practicum.explorewithme.main.dto.comment;

import lombok.*;
import ru.practicum.explorewithme.main.model.comment.CommentStatus;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {
    private Long id;
    private Long eventId;
    private Long authorId;
    private String authorName;
    private String text;
    private LocalDateTime createdOn;
    private CommentStatus status;
}
