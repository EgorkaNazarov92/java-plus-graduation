package ru.practicum.controller.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.service.RequestService;

import java.util.List;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/int/requests")
public class RequestIntController {
	private final RequestService requestService;

	@GetMapping("/{eventId}")
	public List<RequestDto> getEventRequests(@PathVariable Long eventId) {
		log.info("Получить запросы по eventId --> {}", eventId);
		return requestService.getEventRequests(eventId);
	}

	@GetMapping("/{requestIds}")
	public List<RequestDto> getListRequests(@PathVariable List<Long> requestIds) {
		log.info("Получить запросы по requestIds --> {}", requestIds);
		return requestService.getListRequests(requestIds);
	}

	@PostMapping
	public List<RequestDto> saveRequests(@PathVariable List<RequestDto> requests) {
		log.info("Сохранить запросы requests --> {}", requests);
		return requestService.saveRequests(requests);
	}
}
