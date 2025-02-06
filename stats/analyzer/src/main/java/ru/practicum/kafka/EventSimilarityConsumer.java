package ru.practicum.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.service.event.EventSimilarityService;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventSimilarityConsumer {
	private final EventSimilarityService eventSimilarityService;

	@KafkaListener(topics = "getEventSimilarityTopic", containerFactory = "eventSimilarityListenerConsumerFactory")
	public void consumerUserAction(EventSimilarityAvro eventSimilarityAvro) {
		log.info("Получено сообщение: {}", eventSimilarityAvro);
		eventSimilarityService.updateEventSimilarity(eventSimilarityAvro);
	}
}
