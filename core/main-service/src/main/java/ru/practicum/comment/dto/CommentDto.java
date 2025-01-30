package ru.practicum.comment.dto;

import ru.practicum.user.model.User;
import lombok.Data;

@Data
public class CommentDto {
    private Long id;
    private String content;
    private User author;
}
