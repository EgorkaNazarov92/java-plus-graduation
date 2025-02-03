package ru.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.practicum.dto.request.RequestDto;

import java.util.List;

@FeignClient(name = "request-service")
public interface RequestClient {

	@GetMapping("/int/requests/{eventId}")
	public List<RequestDto> getEventRequests(@PathVariable Long eventId);

	@GetMapping("/int/requests/{requestIds}")
	List<RequestDto> getListRequests(@PathVariable List<Long> requestIds);

	@PostMapping("/int/requests/")
	List<RequestDto> saveRequests(@PathVariable List<RequestDto> requests);
}
