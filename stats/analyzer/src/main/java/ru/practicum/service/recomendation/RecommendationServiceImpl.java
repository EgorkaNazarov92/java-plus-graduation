package ru.practicum.service.recomendation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.proto.recomendations.Recomendations;
import ru.practicum.model.EventSimilarity;
import ru.practicum.model.UserAction;
import ru.practicum.repository.EventSimilarityRepository;
import ru.practicum.repository.UserActionRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationServiceImpl implements RecommendationService {
	private final UserActionRepository actionRepository;
	private final EventSimilarityRepository similarityRepository;

	@Override
	public List<Recomendations.RecommendedEventProto> getRecommendationsForUser(
			Recomendations.UserPredictionsRequestProto request) {
		List<UserAction> userActionList = actionRepository.findByUserId(request.getUserId());
		if (userActionList.isEmpty())
			return Collections.emptyList();

		Set<Long> userEventIds = userActionList.stream()
				.map(UserAction::getEventId)
				.collect(Collectors.toSet());

		userActionList.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));

		List<UserAction> actionList = userActionList.subList(0,
				Math.min(request.getMaxResults(), userActionList.size()));

		Set<Long> eventIds = actionList.stream()
				.map(UserAction::getEventId)
				.collect(Collectors.toSet());

		List<EventSimilarity> similarities = similarityRepository.findAllByEventAOrEventBIn(eventIds, eventIds);
		Map<Long, Float> recEvents = new HashMap<>();
		for (UserAction action : actionList) {
			for (EventSimilarity similarity : similarities) {
				Long recEventId = (action.getEventId() == similarity.getEventA())
						? similarity.getEventB() : similarity.getEventA();
				if (userEventIds.contains(recEventId))
					continue;
				if (similarity.getScore() > recEvents.getOrDefault(recEventId, 0f))
					recEvents.put(recEventId, similarity.getScore());
			}
		}
		return recEvents.entrySet().stream()
				.map(event ->
						Recomendations.RecommendedEventProto.newBuilder()
								.setEventId(event.getKey())
								.setScore(event.getValue())
								.build())
				.sorted(Comparator.comparingDouble(Recomendations.RecommendedEventProto::getScore).reversed())
				.limit(request.getMaxResults())
				.toList();

	}

	@Override
	public List<Recomendations.RecommendedEventProto> getSimilarEvents(Recomendations.SimilarEventsRequestProto request) {
		Long eventId = request.getEventId();
		Set<Long> userEventIds = actionRepository.findByUserId(request.getUserId()).stream()
				.map(UserAction::getEventId)
				.collect(Collectors.toSet());

		List<EventSimilarity> similarities = similarityRepository.findAllByEventAOrEventB(eventId, eventId);

		List<Recomendations.RecommendedEventProto> response = new ArrayList<>();
		for (EventSimilarity similaritie : similarities) {
			Long curEventId = (eventId.equals(similaritie.getEventA()))
					? similaritie.getEventB()
					: similaritie.getEventA();
			if (!userEventIds.contains(curEventId))
				response.add(Recomendations.RecommendedEventProto.newBuilder()
						.setEventId(curEventId)
						.setScore(similaritie.getScore())
						.build());

		}
		response.sort((o1, o2) ->
				Double.compare(o2.getScore(), o1.getScore()));
		return response.subList(0, Math.min(response.size(), request.getMaxResults()));
	}

	@Override
	public List<Recomendations.RecommendedEventProto>
	getInteractionsCount(Recomendations.InteractionsCountRequestProto request) {
		List<Long> eventIds = request.getEventIdList();
		List<Recomendations.RecommendedEventProto> result = new ArrayList<>();

		for (Long eventId : eventIds) {
			List<UserAction> actionList = actionRepository.findByEventId(eventId);
			result.add(Recomendations.RecommendedEventProto.newBuilder()
					.setEventId(eventId)
					.setScore(actionList.stream()
							.mapToDouble(UserAction::getMaxWeight).sum())
					.build());
		}
		return result;
	}
}
