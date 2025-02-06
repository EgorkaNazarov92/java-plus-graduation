package ru.practicum.service.useraction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.ActionTypeAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.model.UserAction;
import ru.practicum.repository.UserActionRepository;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class UserActionServiceImpl implements UserActionService {
	private final UserActionRepository repository;

	@Override
	public void updateUserAction(UserActionAvro userActionAvro) {
		double newWeight = getWeight(userActionAvro.getActionType());
		Instant newTimestamp = userActionAvro.getTimestamp();
		UserAction userAction = repository.findByUserIdAndEventId(
				userActionAvro.getUserId(), userActionAvro.getEventId());
		if (userAction == null) {
			userAction = UserAction.builder()
					.userId(userActionAvro.getUserId())
					.eventId(userActionAvro.getEventId())
					.maxWeight(newWeight)
					.timestamp(newTimestamp)
					.build();
		} else {
			if (newWeight > userAction.getMaxWeight())
				userAction.setMaxWeight(newWeight);
			if (newTimestamp.isAfter(userAction.getTimestamp()))
				userAction.setTimestamp(newTimestamp);
		}
		repository.save(userAction);
	}

	private double getWeight(ActionTypeAvro actionTypeAvro) {
		return switch (actionTypeAvro) {
			case VIEW -> 0.4;
			case REGISTER -> 0.8;
			case LIKE -> 1.0;
			default -> 0d;
		};
	}
}
