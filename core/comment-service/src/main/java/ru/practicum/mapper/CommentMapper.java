package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.Comment;

@Mapper
public interface CommentMapper {
	CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

	CommentDto commentToCommentDto(Comment comment);

	@Mapping(target = "id", source = "userId")
	UserDto userToUserDto(Long userId);
}
