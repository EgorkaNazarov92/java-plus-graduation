package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.ewm.stats.proto.useraction.UserActionProto;
import ru.practicum.kafka.config.KafkaConfig;
import ru.practicum.mapper.UserActionMapper;

@Service
@RequiredArgsConstructor
public class UserActionServiceImpl implements UserActionService {
	private final KafkaTemplate<String, UserActionAvro> kafkaTemplate;
	private final KafkaConfig config;

	@Override
	public void collectUserAction(UserActionProto request) {
		UserActionAvro userActionAvro = UserActionMapper.mapToAvro(request);
		kafkaTemplate.send(config.getTopicOut(), userActionAvro);
	}
}
