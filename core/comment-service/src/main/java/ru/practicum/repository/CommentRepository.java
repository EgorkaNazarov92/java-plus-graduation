package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByEventAndAuthor(Long eventId, Long authorId);

	List<Comment> findAllByEvent(Long eventId);
}
