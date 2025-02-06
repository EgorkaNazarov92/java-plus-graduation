package ru.practicum.mapper;

import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.proto.useraction.ActionTypeProto;
import ru.practicum.ewm.stats.proto.useraction.UserActionProto;

import java.time.Instant;

public class UserActionMapper {

	public static UserActionAvro mapToAvro(UserActionProto userActionProto) {
		return UserActionAvro.newBuilder()
				.setUserId(userActionProto.getUserId())
				.setEventId(userActionProto.getEventId())
				.setActionType(mapTypeToAvro(userActionProto.getActionType()))
				.setTimestamp(Instant.ofEpochSecond(userActionProto.getTimestamp().getSeconds(),
						userActionProto.getTimestamp().getNanos()))
				.build();
	}

	private static ActionTypeAvro mapTypeToAvro(ActionTypeProto actionTypeProto) {
		return switch (actionTypeProto) {
			case ACTION_VIEW -> ActionTypeAvro.VIEW;
			case ACTION_REGISTER -> ActionTypeAvro.REGISTER;
			case ACTION_LIKE -> ActionTypeAvro.LIKE;
			default -> null;
		};
	}
}
