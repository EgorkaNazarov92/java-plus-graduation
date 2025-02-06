package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.EventSimilarity;

import java.util.List;
import java.util.Set;

@Repository
public interface EventSimilarityRepository extends JpaRepository<EventSimilarity, Long> {
	EventSimilarity findByEventAAndEventB(Long eventA, Long eventB);

	List<EventSimilarity> findAllByEventAOrEventBIn(Set<Long> eventAList, Set<Long> eventBList);

	List<EventSimilarity> findAllByEventAOrEventB(Long eventA, Long eventB);
}
