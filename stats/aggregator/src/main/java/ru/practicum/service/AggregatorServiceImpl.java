package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.kafka.config.KafkaConfig;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class AggregatorServiceImpl implements AggregatorService {
	private final Map<Long, Map<Long, Double>> usersActions = new HashMap<>();
	private final Map<Long, Double> eventsWeightSums = new HashMap<>();
	private final Map<Long, Map<Long, Double>> minWeightsSums = new HashMap<>();

	private final KafkaTemplate<String, EventSimilarityAvro> kafkaTemplate;
	private final KafkaConfig config;

	public void processUserAction(UserActionAvro userActionAvro) {
		Long userId = userActionAvro.getUserId();
		Long eventId = userActionAvro.getEventId();
		double weight = getWeight(userActionAvro.getActionType());

		Map<Long, Double> usersActionWeight = usersActions.computeIfAbsent(eventId,
				aLong -> new HashMap<>());
		double oldWeight = usersActionWeight.getOrDefault(userId, 0d);

		if (weight > oldWeight) {
			usersActionWeight.put(userId, weight);
			double oldWeightSum = eventsWeightSums.getOrDefault(eventId, 0d);
			double diff = weight - oldWeight;
			eventsWeightSums.put(eventId, oldWeightSum + diff);
			minWeightsSums.keySet()
					.stream()
					.filter(event -> event.equals(eventId))
					.forEach(event -> updateSimilarity(eventId, event,
							userActionAvro.getTimestamp()));
		}
	}

	private double getWeight(ActionTypeAvro actionTypeAvro) {
		return switch (actionTypeAvro) {
			case VIEW -> 0.4;
			case REGISTER -> 0.8;
			case LIKE -> 1.0;
			default -> 0d;
		};
	}


	private void updateSimilarity(Long eventA, Long eventB, Instant timestamp) {
		double sMin = calcMin(eventA, eventB);
		putMinWeightSum(eventA, eventB, sMin);

		double sA = eventsWeightSums.getOrDefault(eventA, 0d);
		double sB = eventsWeightSums.getOrDefault(eventB, 0d);
		if (sA != 0 || sB != 0) {

			float similarity = (float) (sMin / (sA * sB));

			long first = Math.min(eventA, eventB);
			long second = Math.max(eventA, eventB);

			EventSimilarityAvro eventSimilarityAvro = EventSimilarityAvro.newBuilder()
					.setEventA(first)
					.setEventB(second)
					.setScore(similarity)
					.setTimestamp(timestamp)
					.build();

			kafkaTemplate.send(config.getTopicOut(), eventSimilarityAvro);
		}
	}


	private double calcMin(long eventA, long eventB) {
		Map<Long, Double> actionA = usersActions.getOrDefault(eventA, Map.of());
		Map<Long, Double> actionB = usersActions.getOrDefault(eventB, Map.of());

		return actionA.entrySet().stream()
				.filter(e -> actionB.get(e.getKey()) != null)
				.mapToDouble(e -> Math.min(e.getValue(), actionB.get(e.getKey())))
				.sum();
	}


	public void putMinWeightSum(Long eventA, Long eventB, double sum) {
		Long first = Math.min(eventA, eventB);
		Long second = Math.max(eventA, eventB);
		minWeightsSums.computeIfAbsent(first, k -> new HashMap<>()).put(second, sum);
	}
}
