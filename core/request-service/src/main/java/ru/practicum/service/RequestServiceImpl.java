package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.UserClient;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.types.EventState;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.types.RequestStatus;
import ru.practicum.dto.user.UserDto;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.EventClient;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Request;
import ru.practicum.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
	private final RequestRepository requestRepository;
	private final EventClient eventClient;
	private final UserClient userClient;

	@Override
	public List<RequestDto> getRequests(Long userId) {
		getUser(userId);
		return RequestMapper.INSTANCE.mapListRequests(requestRepository.findAllByRequester(userId));
	}

	@Transactional
	@Override
	public RequestDto createRequest(Long userId, Long eventId) {
		EventDto event = getEvent(eventId);
		UserDto user = getUser(userId);
		checkRequest(userId, event);
		Request request = Request.builder()
				.requester(user.getId())
				.created(LocalDateTime.now())
				.status(!event.getRequestModeration()
						|| event.getParticipantLimit() == 0
						? RequestStatus.CONFIRMED : RequestStatus.PENDING)
				.event(event.getId())
				.build();
		request = requestRepository.save(request);
		if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
			event.setConfirmedRequests(event.getConfirmedRequests() + 1);
			eventClient.saveEvent(event);
		}
		return RequestMapper.INSTANCE.mapToRequestDto(request);
	}

	@Transactional
	@Override
	public RequestDto cancelRequest(Long userId, Long requestId) {
		getUser(userId);
		Request request = getRequest(requestId);
		if (!request.getRequester().equals(userId))
			throw new ConflictException("Другой пользователь не может отменить запрос");
		request.setStatus(RequestStatus.CANCELED);
		requestRepository.save(request);
		EventDto event = getEvent(request.getEvent());
		event.setConfirmedRequests(event.getConfirmedRequests() - 1);
		eventClient.saveEvent(event);
		return RequestMapper.INSTANCE.mapToRequestDto(request);
	}

	@Override
	public List<RequestDto> getEventRequests(Long eventId) {
		return RequestMapper.INSTANCE.mapListRequests(requestRepository.findAllByEvent(eventId));
	}

	@Override
	public List<RequestDto> getListRequests(List<Long> requestIds) {
		return RequestMapper.INSTANCE.mapListRequests(requestRepository.findAllById(requestIds));
	}

	@Override
	public List<RequestDto> saveRequests(List<RequestDto> requests) {
		return RequestMapper.INSTANCE.mapListRequests(
				requestRepository.saveAll(RequestMapper.INSTANCE.mapListDtoRequests(requests)));
	}

	private void checkRequest(Long userId, EventDto event) {
		if (!requestRepository.findAllByRequesterAndEvent(userId, event.getId()).isEmpty())
			throw new ConflictException("нельзя добавить повторный запрос");
		if (event.getInitiator().getId().equals(userId))
			throw new ConflictException("инициатор события не может добавить запрос на участие в своём событии");
		if (!event.getState().equals(EventState.PUBLISHED))
			throw new ConflictException("нельзя участвовать в неопубликованном событии");
		if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests()))
			throw new ConflictException("у события достигнут лимит запросов на участие");
	}

	private EventDto getEvent(Long eventId) {
		Optional<EventDto> event = Optional.ofNullable(eventClient.getEvent(eventId));
		if (event.isEmpty())
			throw new NotFoundException("События с id = " + eventId.toString() + " не существует");
		return event.get();
	}

	private UserDto getUser(Long userId) {
		Optional<UserDto> user = Optional.ofNullable(userClient.getUser(userId));
		if (user.isEmpty())
			throw new NotFoundException("Пользователя с id = " + userId.toString() + " не существует");
		return user.get();
	}

	private Request getRequest(Long requestId) {
		Optional<Request> request = requestRepository.findById(requestId);
		if (request.isEmpty())
			throw new NotFoundException("Запроса с id = " + requestId.toString() + " не существует");
		return request.get();
	}
}
