package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.UserClient;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.CreateCommentDto;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.user.UserDto;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.EventClient;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
	private final CommentRepository commentRepository;
	private final UserClient userClient;
	private final EventClient eventClient;

	private static final String COMMENT_NOT_FOUND = "Comment not found";

	@Override
	@Transactional
	public CommentDto addComment(Long userId, Long eventId, CreateCommentDto createCommentDto) {
		UserDto user = getUserById(userId);
		EventDto event = getEventById(eventId);

		Comment comment = Comment.builder()
				.author(user.getId())
				.event(event.getId())
				.content(createCommentDto.getContent())
				.build();

		Comment saved = commentRepository.save(comment);
		return CommentMapper.INSTANCE.commentToCommentDto(saved);
	}

	@Override
	public CommentDto getComment(Long eventId, Long commentId) {
		getEventById(eventId);
		return CommentMapper.INSTANCE.commentToCommentDto(getCommentById(commentId));
	}

	@Override
	public List<CommentDto> getEventCommentsByUserId(Long userId, Long eventId) {
		getUserById(userId);
		getEventById(eventId);
		return commentRepository.findAllByEventAndAuthor(eventId, userId)
				.stream()
				.map(CommentMapper.INSTANCE::commentToCommentDto)
				.toList();
	}

	@Override
	public List<CommentDto> getEventComments(Long eventId) {
		getEventById(eventId);
		return commentRepository.findAllByEvent(eventId)
				.stream()
				.map(CommentMapper.INSTANCE::commentToCommentDto)
				.toList();
	}

	@Override
	@Transactional
	public CommentDto updateComment(Long userId, Long eventId, Long commentId, CreateCommentDto createCommentDto) {
		getEventById(eventId);
		Comment comment = getCommentById(commentId);
		UserDto user = getUserById(userId);
		if (!Objects.equals(comment.getAuthor(), user.getId())) {
			throw new ConflictException("This user can't update comment");
		}
		comment.setContent(createCommentDto.getContent());
		return CommentMapper.INSTANCE.commentToCommentDto(commentRepository.save(comment));
	}

	@Override
	@Transactional
	public void deleteComment(Long userId, Long eventId, Long commentId) {
		getEventById(eventId);
		Comment comment = getCommentById(commentId);
		UserDto user = getUserById(userId);
		if (!Objects.equals(comment.getAuthor(), user.getId())) {
			throw new ConflictException("This user can't delete comment");
		}
		commentRepository.deleteById(commentId);
	}

	private UserDto getUserById(Long userId) {
		Optional<UserDto> optionalUser = Optional.ofNullable(userClient.getUser(userId));
		if (optionalUser.isEmpty()) {
			throw new NotFoundException("User not found");
		}
		return optionalUser.get();
	}

	private EventDto getEventById(Long eventId) {
		Optional<EventDto> optionalEvent = Optional.ofNullable(eventClient.getEvent(eventId));
		if (optionalEvent.isEmpty()) {
			throw new NotFoundException("Event not found");
		}
		return optionalEvent.get();
	}

	private Comment getCommentById(Long commentId) {
		Optional<Comment> optionalComment = commentRepository.findById(commentId);
		if (optionalComment.isEmpty()) {
			throw new NotFoundException(COMMENT_NOT_FOUND);
		}
		return optionalComment.get();
	}
}
