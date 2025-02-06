package ru.practicum.controller;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.proto.recomendations.Recomendations;
import ru.practicum.ewm.stats.proto.recomendations.RecommendationsControllerGrpc;
import ru.practicum.service.recomendation.RecommendationService;

import java.util.List;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class RecommendationsController extends RecommendationsControllerGrpc.RecommendationsControllerImplBase {
	private final RecommendationService recommendationService;

	@Override
	public void getRecommendationsForUser(Recomendations.UserPredictionsRequestProto request,
										  StreamObserver<Recomendations.RecommendedEventProto> response) {
		try {
			log.info("Пришел запрос: {}", request);
			List<Recomendations.RecommendedEventProto> eventList = recommendationService.getRecommendationsForUser(request);
			for (Recomendations.RecommendedEventProto eventProto : eventList) {
				response.onNext(eventProto);
			}
			response.onCompleted();
		} catch (Exception e) {
			log.error("Неизвестная ошибка: {}", e.getMessage(), e);
			response.onError(new StatusRuntimeException(
					Status.INTERNAL
							.withDescription(e.getLocalizedMessage())
							.withCause(e)
			));
		}
	}

	@Override
	public void getSimilarEvents(Recomendations.SimilarEventsRequestProto request,
								 StreamObserver<Recomendations.RecommendedEventProto> response) {
		try {
			log.info("Пришел запрос: {}", request);
			List<Recomendations.RecommendedEventProto> eventList = recommendationService.getSimilarEvents(request);
			for (Recomendations.RecommendedEventProto eventProto : eventList) {
				response.onNext(eventProto);
			}
			response.onCompleted();
		} catch (Exception e) {
			log.error("Неизвестная ошибка: {}", e.getMessage(), e);
			response.onError(new StatusRuntimeException(
					Status.INTERNAL
							.withDescription(e.getLocalizedMessage())
							.withCause(e)
			));
		}
	}

	@Override
	public void getInteractionsCount(Recomendations.InteractionsCountRequestProto request,
									 StreamObserver<Recomendations.RecommendedEventProto> response) {
		try {
			log.info("Пришел запрос: {}", request);
			List<Recomendations.RecommendedEventProto> eventList = recommendationService.getInteractionsCount(request);
			for (Recomendations.RecommendedEventProto eventProto : eventList) {
				response.onNext(eventProto);
			}
			response.onCompleted();
		} catch (Exception e) {
			log.error("Неизвестная ошибка: {}", e.getMessage(), e);
			response.onError(new StatusRuntimeException(
					Status.INTERNAL
							.withDescription(e.getLocalizedMessage())
							.withCause(e)
			));
		}
	}
}
