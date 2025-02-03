package ru.practicum.comment.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.comment.model.Comment;
import ru.practicum.dto.comment.CommentDto;

@Mapper
public interface CommentMapper {
	CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

	CommentDto commentToCommentDto(Comment comment);
}
