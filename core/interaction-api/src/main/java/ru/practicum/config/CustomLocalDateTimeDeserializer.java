package ru.practicum.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String dateTime = p.getText();
		try {
			return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} catch (DateTimeParseException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}