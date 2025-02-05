package ru.practicum.dto.event;

import lombok.Builder;
import lombok.Data;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.event.types.EventState;
import ru.practicum.dto.user.UserDto;

import java.time.LocalDateTime;

@Data
@Builder
public class EventClientDto {
	private Long id;
	private String annotation;
	private Integer confirmedRequests;
	private LocalDateTime createdOn;
	private String description;
	private LocalDateTime eventDate;
	private Boolean paid;
	private Integer participantLimit;
	private LocalDateTime publishedOn;
	private Boolean requestModeration;
	private EventState state;
	private String title;
	private Long views;
	private CategoryDto category;
	private UserDto initiator;
	private LocationDto location;

}