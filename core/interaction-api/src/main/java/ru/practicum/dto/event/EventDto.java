package ru.practicum.dto.event;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import ru.practicum.config.CustomLocalDateTimeDeserializer;
import ru.practicum.config.CustomLocalDateTimeSerializer;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.user.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventDto {
	private Long id;
	private String annotation;
	private Integer confirmedRequests;
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime createdOn;
	private String description;
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime eventDate;
	private Boolean paid;
	private Integer participantLimit;
	@JsonDeserialize(using = CustomLocalDateTimeDeserializer.class)
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	private LocalDateTime publishedOn;
	private Boolean requestModeration;
	private String state;
	private String title;
	private Long views;
	private CategoryDto category;
	private UserDto initiator;
	private LocationDto location;

}
