package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import ru.practicum.dto.event.types.StateAction;

import java.time.LocalDateTime;


@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventDto {
	protected String annotation;
	protected Long category;
	protected String description;
	@JsonProperty("eventDate")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
	protected LocalDateTime eventDate;
	protected LocationDto location;
	protected Boolean paid;
	@PositiveOrZero
	protected Integer participantLimit;
	protected Boolean requestModeration;
	protected StateAction stateAction;
	protected String title;
}
