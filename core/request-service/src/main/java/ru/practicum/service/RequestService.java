package ru.practicum.service;


import ru.practicum.dto.request.RequestDto;

import java.util.List;

public interface RequestService {
	List<RequestDto> getRequests(Long userId);

	RequestDto createRequest(Long userId, Long eventId);

	RequestDto cancelRequest(Long userId, Long requestId);

	List<RequestDto> getEventRequests(Long eventId);

	List<RequestDto> getListRequests(List<Long> requestIds);

	List<RequestDto> saveRequests(List<RequestDto> requests);
}
