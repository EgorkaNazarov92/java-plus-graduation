package ru.practicum.controller;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.practicum.ewm.stats.proto.useraction.UserActionControllerGrpc;
import ru.practicum.ewm.stats.proto.useraction.UserActionProto;
import ru.practicum.service.UserActionService;

@Slf4j
@GrpcService
@RequiredArgsConstructor
public class UserActionController extends UserActionControllerGrpc.UserActionControllerImplBase {
	private final UserActionService userActionService;

	@Override
	public void collectUserAction(UserActionProto request, StreamObserver<Empty> responseObserver) {
		try {
			log.info("Пришел запрос: {}", request);
			userActionService.collectUserAction(request);

			responseObserver.onNext(Empty.getDefaultInstance());
			responseObserver.onCompleted();
		} catch (Exception e) {
			log.error("Неизвестная ошибка: {}", e.getMessage(), e);
			responseObserver.onError(new StatusRuntimeException(
					Status.INTERNAL
							.withDescription(e.getLocalizedMessage())
							.withCause(e)
			));
		}
	}
}
