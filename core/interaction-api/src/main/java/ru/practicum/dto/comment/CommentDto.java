package ru.practicum.dto.comment;

import lombok.Data;
import ru.practicum.dto.user.UserDto;

@Data
public class CommentDto {
	private Long id;
	private String content;
	private UserDto author;
}
