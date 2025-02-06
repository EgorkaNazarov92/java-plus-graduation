package ru.practicum.kafka.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import ru.practicum.ewm.stats.avro.EventSimilarityAvro;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.serializer.EventSimilarityDeserializer;
import ru.practicum.serializer.UserActionDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KafkaConfig {
	@Value("${analyzer.kafka.bootstrap.server}")
	String bootstrapServer;
	@Value("${analyzer.kafka.group-id}")
	String groupId;
	@Getter
	@Value("${analyzer.kafka.topic.user-action}")
	String topicUserAction;
	@Getter
	@Value("${analyzer.kafka.topic.event-similarity}")
	String topicEventSimilarity;

	@Bean
	public String getUserActionTopic() {
		return this.getTopicUserAction();
	}

	@Bean
	public String getEventSimilarityTopic() {
		return this.getTopicEventSimilarity();
	}

	@Bean
	public ConsumerFactory<String, UserActionAvro> userActionConsumerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UserActionDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, UserActionAvro>
	userActionListenerConsumerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, UserActionAvro> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(userActionConsumerFactory());
		return factory;
	}

	@Bean
	public ConsumerFactory<String, EventSimilarityAvro> userEventSimilarityFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
		configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, EventSimilarityDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(configProps);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, EventSimilarityAvro>
	eventSimilarityListenerConsumerFactory() {

		ConcurrentKafkaListenerContainerFactory<String, EventSimilarityAvro> factory =
				new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(userEventSimilarityFactory());
		return factory;
	}
}
