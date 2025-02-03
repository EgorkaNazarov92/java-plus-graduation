package ru.practicum.event.controller.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDto;
import ru.practicum.event.EventService;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/int/events")
public class InternalEventController {
	private final EventService service;

	@PostMapping
	public EventDto saveEvent(@RequestBody EventDto event) {
		log.info("Запрос на сохранение event -> {}", event);
		return service.saveEvent(event);
	}

	@GetMapping("/{eventId}")
	public EventDto getEvent(@PathVariable Long eventId) {
		log.info("Получить событие по eventId --> {}", eventId);
		return service.getIntEvent(eventId);
	}
}
