package ru.practicum.kafka.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConfig {
	@Getter
	@Value("${collector.kafka.bootstrap.server}")
	String bootstrapServer;
	@Getter
	@Value("${collector.kafka.topic.out}")
	String topicOut;
}
