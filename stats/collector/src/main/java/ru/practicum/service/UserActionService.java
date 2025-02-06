package ru.practicum.service;

import ru.practicum.ewm.stats.proto.useraction.UserActionProto;

public interface UserActionService {
	void collectUserAction(UserActionProto request);
}
