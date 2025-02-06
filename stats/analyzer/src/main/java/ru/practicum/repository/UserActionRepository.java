package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.UserAction;

import java.util.List;

@Repository
public interface UserActionRepository extends JpaRepository<UserAction, Long> {
	UserAction findByUserIdAndEventId(Long userId, Long eventId);

	List<UserAction> findByUserId(Long userId);

	List<UserAction> findByEventId(Long eventId);
}
