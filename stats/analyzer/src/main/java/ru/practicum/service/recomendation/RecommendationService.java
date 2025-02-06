package ru.practicum.service.recomendation;

import ru.practicum.ewm.stats.proto.recomendations.Recomendations;

import java.util.List;

public interface RecommendationService {
	List<Recomendations.RecommendedEventProto> getRecommendationsForUser(Recomendations.UserPredictionsRequestProto request);

	List<Recomendations.RecommendedEventProto> getSimilarEvents(Recomendations.SimilarEventsRequestProto request);

	List<Recomendations.RecommendedEventProto> getInteractionsCount(Recomendations.InteractionsCountRequestProto request);
}
