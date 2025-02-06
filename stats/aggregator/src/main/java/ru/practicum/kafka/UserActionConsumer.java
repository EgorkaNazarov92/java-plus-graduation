package ru.practicum.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.service.AggregatorService;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActionConsumer {
	private final AggregatorService aggregatorService;

	@KafkaListener(topics = "getTopicIn", containerFactory = "userActionListenerConsumerFactory")
	public void consumerUserAction(UserActionAvro userActionAvro) {
		log.info("Получено сообщение: {}", userActionAvro);
		aggregatorService.processUserAction(userActionAvro);
	}
}
