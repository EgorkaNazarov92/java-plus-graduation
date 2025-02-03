package ru.practicum.event;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.event.EventDto;

@FeignClient(name = "event-service")
public interface EventClient {

	@PostMapping("/int/events")
	EventDto saveEvent(@RequestBody EventDto event);


	@GetMapping("/int/events/{eventId}")
	EventDto getEvent(@PathVariable Long eventId);
}
