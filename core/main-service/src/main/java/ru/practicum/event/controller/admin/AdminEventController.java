package ru.practicum.event.controller.admin;

import ru.practicum.event.EventService;
import ru.practicum.event.dto.AdminGetEventRequestDto;
import ru.practicum.event.dto.EventDto;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.validate.EventValidate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
	private final EventService eventService;

	@GetMapping
	public List<EventDto> adminGetEvents(AdminGetEventRequestDto requestParams) {
		log.info("Получить события, согласно устловиям -> {}", requestParams);
		return eventService.adminGetEvents(requestParams);
	}

	@PatchMapping("/{eventId}")
	public EventDto adminChangeEvent(@PathVariable Long eventId,
									 @RequestBody @Valid UpdateEventDto eventDto) {
		log.info("Изменить событие eventId = {}, поля -> {}", eventId, eventDto);
		EventValidate.updateEventDateValidate(eventDto, log);
		EventValidate.textLengthValidate(eventDto, log);
		return eventService.adminChangeEvent(eventId, eventDto);
	}
}
