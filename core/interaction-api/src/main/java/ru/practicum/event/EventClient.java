package ru.practicum.event;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.event.EventClientDto;

@FeignClient(name = "event-service")
public interface EventClient {

	@PostMapping("/int/events")
	EventClientDto saveEvent(@RequestBody EventClientDto event);


	@GetMapping(value = "/int/events/{eventId}", produces = {MediaType.APPLICATION_JSON_VALUE})
	EventClientDto getEvent(@PathVariable Long eventId);
}
