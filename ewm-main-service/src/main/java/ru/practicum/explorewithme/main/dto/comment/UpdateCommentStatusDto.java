package ru.practicum.explorewithme.main.dto.comment;

import lombok.*;
import jakarta.validation.constraints.*;
import ru.practicum.explorewithme.main.model.comment.CommentStatus;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentStatusDto {
    @NotNull(message = "List of comment IDs must not be null")
    private List<@NotNull(message = "Comment ID must not be null") Long> commentIds;

    @NotNull(message = "Status must not be null")
    private CommentStatus status;
}
