package ru.practicum.service.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.model.EventSimilarity;
import ru.practicum.repository.EventSimilarityRepository;


@Service
@RequiredArgsConstructor
public class EventSimilarityServiceImpl implements EventSimilarityService {
	private final EventSimilarityRepository repository;

	@Override
	public void updateEventSimilarity(EventSimilarityAvro eventSimilarityAvro) {
		Long eventA = eventSimilarityAvro.getEventA();
		Long eventB = eventSimilarityAvro.getEventB();
		EventSimilarity eventSimilarity = repository.findByEventAAndEventB(eventA, eventB);
		if (eventSimilarity == null) {
			eventSimilarity = EventSimilarity.builder()
					.eventA(eventA)
					.eventB(eventB)
					.score(eventSimilarityAvro.getScore())
					.timestamp(eventSimilarityAvro.getTimestamp())
					.build();
		} else {
			eventSimilarity.setScore(eventSimilarityAvro.getScore());
			eventSimilarity.setTimestamp(eventSimilarityAvro.getTimestamp());
		}
		repository.save(eventSimilarity);
	}
}
