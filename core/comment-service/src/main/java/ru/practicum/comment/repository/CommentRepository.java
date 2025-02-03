package ru.practicum.comment.repository;

import ru.practicum.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventIdAndAuthorId(Long eventId, Long authorId);

    List<Comment> findAllByEventId(Long eventId);
}
