package ru.practicum.kafka;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.practicum.ewm.stats.avro.UserActionAvro;
import ru.practicum.kafka.config.KafkaConfig;
import ru.practicum.serializer.BaseAvroSerializer;

import java.util.Map;

@Getter
@Setter
@Configuration
@RequiredArgsConstructor
public class KafkaUserProducer {
	private final KafkaConfig config;

	@Bean
	public ProducerFactory<String, UserActionAvro> producerFactory() {
		Map<String, Object> props = Map.of(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, config.getBootstrapServer(),
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, BaseAvroSerializer.class
		);
		return new DefaultKafkaProducerFactory<>(props);
	}

	@Bean
	public KafkaTemplate<String, UserActionAvro> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}
}
