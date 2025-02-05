package ru.practicum.request;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.practicum.dto.request.RequestDto;

import java.util.List;

@FeignClient(name = "request-service")
public interface RequestClient {

	@GetMapping("/int/requests/event/{eventId}")
	List<RequestDto> getEventRequests(@PathVariable Long eventId);

	@GetMapping("/int/requests/{requestIds}")
	List<RequestDto> getListRequests(@PathVariable List<Long> requestIds);

	@PostMapping("/int/requests")
	List<RequestDto> saveRequests(@RequestBody List<RequestDto> requests);
}
