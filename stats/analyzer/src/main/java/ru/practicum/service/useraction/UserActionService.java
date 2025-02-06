package ru.practicum.service.useraction;

import ru.practicum.ewm.stats.avro.UserActionAvro;

public interface UserActionService {
	void updateUserAction(UserActionAvro userActionAvro);
}
