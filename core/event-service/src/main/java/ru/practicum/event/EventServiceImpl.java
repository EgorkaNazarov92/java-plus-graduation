package ru.practicum.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.event.*;
import ru.practicum.dto.event.types.EventState;
import ru.practicum.dto.event.types.StateAction;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.types.RequestStatus;
import ru.practicum.dto.user.UserDto;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.error.exception.ValidationException;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.request.RequestClient;
import ru.practicum.service.StatisticsService;
import ru.practicum.user.UserClient;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
	private final EventRepository repository;
	private final UserClient userClient;
	private final CategoryRepository categoryRepository;
	private final StatisticsService statisticsService;
	private final RequestClient requestClient;

	private static final String EVENT_NOT_FOUND_MESSAGE = "Event not found";

	@Override
	public List<EventDto> getEvents(Long userId, Integer from, Integer size) {
		getUser(userId);
		Pageable pageable = PageRequest.of(from, size);
		return repository.findByInitiatorId(userId, pageable).stream()
				.map(this::eventToDto)
				.toList();
	}

	@Override
	public EventDto getEventById(Long userId, Long id, String ip, String uri) {
		getUser(userId);
		Optional<Event> event = repository.findByIdAndInitiatorId(id, userId);
		if (event.isEmpty()) {
			throw new NotFoundException(EVENT_NOT_FOUND_MESSAGE);
		}
		return eventToDto(event.get());
	}

	@Override
	public EventDto createEvent(Long userId, CreateEventDto eventDto) {
		UserDto user = getUser(userId);
		Category category = getCategory(eventDto.getCategory());
		Event event = EventMapper.mapCreateDtoToEvent(eventDto);
		if (event.getPaid() == null) {
			event.setPaid(false);
		}
		if (event.getParticipantLimit() == null) {
			event.setParticipantLimit(0);
		}
		if (event.getRequestModeration() == null) {
			event.setRequestModeration(true);
		}

		event.setInitiatorId(user.getId());
		event.setInitiator(user);
		event.setCategory(category);
		event.setState(EventState.PENDING);
		Event newEvent = repository.save(event);
		return eventToDto(newEvent);
	}

	@Override
	public EventDto updateEvent(Long userId, UpdateEventDto eventDto, Long eventId) {
		getUser(userId);
		Optional<Event> eventOptional = repository.findById(eventId);
		if (eventOptional.isEmpty()) {
			throw new NotFoundException(EVENT_NOT_FOUND_MESSAGE);
		}
		Event foundEvent = eventOptional.get();
		if (foundEvent.getState() == EventState.PUBLISHED) {
			throw new ConflictException("Нельзя изменять изменять сообщение, которое опубликовано");
		}
		updateEventFields(eventDto, foundEvent);
		Event saved = repository.save(foundEvent);
		return eventToDto(saved);
	}

	@Override
	public List<EventDto> publicGetEvents(PublicGetEventRequestDto requestParams,
										  HttpServletRequest request) {
		LocalDateTime start = (requestParams.getRangeStart() == null) ?
				LocalDateTime.now() : requestParams.getRangeStart();
		LocalDateTime end = (requestParams.getRangeEnd() == null) ?
				LocalDateTime.now().plusYears(10) : requestParams.getRangeEnd();

		if (start.isAfter(end))
			throw new ValidationException("Дата окончания, должна быть больше даты старта.");
		List<Event> events = repository.findEventsPublic(
				requestParams.getText(),
				requestParams.getCategories(),
				requestParams.getPaid(),
				start,
				end,
				EventState.PUBLISHED,
				requestParams.getOnlyAvailable(),
				PageRequest.of(requestParams.getFrom() / requestParams.getSize(),
						requestParams.getSize())
		);

		statisticsService.saveStats(request);
		if (!events.isEmpty()) {
			LocalDateTime oldestEventPublishedOn = events.stream()
					.min(Comparator.comparing(Event::getPublishedOn)).map(Event::getPublishedOn).stream()
					.findFirst().orElseThrow();
			List<String> uris = getListOfUri(events, request.getRequestURI());

			Map<Long, Long> views = statisticsService.getStats(oldestEventPublishedOn, LocalDateTime.now(), uris);
			events
					.stream()
					.peek(event -> {
						event.setViews(views.get(event.getId()));
						event.setInitiator(getUser(event.getInitiatorId()));
					});
			events = repository.saveAll(events);
		}
		return EventMapper.mapToEventDto(events);
	}

	@Override
	public EventDto publicGetEvent(Long id, HttpServletRequest request) {
		Event event = getEvent(id);
		if (event.getState() != EventState.PUBLISHED) {
			throw new NotFoundException("Событие не найдено");
		}
		statisticsService.saveStats(request);
		Long views = statisticsService.getStats(
				event.getPublishedOn(), LocalDateTime.now(), List.of(request.getRequestURI())).get(id);
		event.setViews(views);
		event = repository.save(event);
		event.setInitiator(getUser(event.getInitiatorId()));
		return EventMapper.mapEventToEventDto(event);
	}

	@Override
	public List<RequestDto> getEventRequests(Long userId, Long eventId) {
		getUser(userId);
		getEvent(eventId);
		return requestClient.getEventRequests(eventId);
	}

	@Override
	public EventRequestStatusUpdateResult changeStatusEventRequests(Long userId, Long eventId,
																	EventRequestStatusUpdateRequest request) {
		getUser(userId);
		Event event = getEvent(eventId);
		EventRequestStatusUpdateResult response = new EventRequestStatusUpdateResult();
		List<RequestDto> requests = requestClient.getListRequests(request.getRequestIds());
		if (request.getStatus().equals(RequestStatus.REJECTED)) {
			checkRequestsStatus(requests);
			requests.stream()
					.map(tmpReq -> changeStatus(tmpReq, RequestStatus.REJECTED))
					.collect(Collectors.toList());
			requestClient.saveRequests(requests);
			response.setRejectedRequests(requests);
		} else {
			if (requests.size() + event.getConfirmedRequests() > event.getParticipantLimit())
				throw new ConflictException("Превышен лимит заявок");
			requests.stream()
					.map(tmpReq -> changeStatus(tmpReq, RequestStatus.CONFIRMED))
					.collect(Collectors.toList());
			requestClient.saveRequests(requests);
			event.setConfirmedRequests(event.getConfirmedRequests() + requests.size());
			repository.save(event);
			response.setConfirmedRequests(requests);
		}
		return response;
	}

	@Override
	public EventClientDto saveEvent(EventClientDto event) {
		Event origEvent = getEvent(event.getId());
		if (event.getConfirmedRequests() != null
				&& !event.getConfirmedRequests().equals(origEvent.getConfirmedRequests())) {
			origEvent.setConfirmedRequests(event.getConfirmedRequests());
		}
		origEvent.setInitiator(getUser(origEvent.getInitiatorId()));
		return EventMapper.mapEventToEventClientDto(repository.save(origEvent));
	}

	@Override
	public EventClientDto getIntEvent(Long eventId) {
		Event event = getEvent(eventId);
		event.setInitiator(getUser(event.getInitiatorId()));
		return EventMapper.mapEventToEventClientDto(event);
	}

	@Override
	public List<EventDto> adminGetEvents(AdminGetEventRequestDto requestParams) {
		List<Event> events = repository.findEventsByAdmin(
				requestParams.getUsers(),
				requestParams.getStates(),
				requestParams.getCategories(),
				requestParams.getRangeStart(),
				requestParams.getRangeEnd(),
				PageRequest.of(requestParams.getFrom() / requestParams.getSize(),
						requestParams.getSize())
		);
		events.stream()
				.peek(event -> event.setInitiator(getUser(event.getInitiatorId())));
		return EventMapper.mapToEventDto(events);
	}

	@Override
	public EventDto adminChangeEvent(Long eventId, UpdateEventDto eventDto) {
		Event event = getEvent(eventId);
		checkEventForUpdate(event, eventDto.getStateAction());
		Event updatedEvent = repository.save(prepareEventForUpdate(event, eventDto));
		updatedEvent.setInitiator(getUser(updatedEvent.getInitiatorId()));
		EventDto result = EventMapper.mapEventToEventDto(updatedEvent);
		return result;
	}

	private EventDto eventToDto(Event event) {
		event.setInitiator(getUser(event.getInitiatorId()));
		return EventMapper.mapEventToEventDto(event);
	}

	private Event getEvent(Long eventId) {
		return repository.findById(eventId)
				.orElseThrow(() -> new NotFoundException(EVENT_NOT_FOUND_MESSAGE));
	}

	private void checkEventForUpdate(Event event, StateAction action) {
		checkEventDate(event.getEventDate());
		if (action == null) return;
		if (action.equals(StateAction.PUBLISH_EVENT)
				&& !event.getState().equals(EventState.PENDING))
			throw new ConflictException("Опубликовать событие можно в статусе PENDING, а статус = "
					+ event.getState());
		if (action.equals(StateAction.REJECT_EVENT)
				&& event.getState().equals(EventState.PUBLISHED))
			throw new ConflictException("Отменить событие можно только в статусе PUBLISHED, а статус = "
					+ event.getState());
	}

	private Event prepareEventForUpdate(Event event, UpdateEventDto updateEventDto) {
		if (updateEventDto.getAnnotation() != null)
			event.setAnnotation(updateEventDto.getAnnotation());
		if (updateEventDto.getDescription() != null)
			event.setDescription(updateEventDto.getDescription());
		if (updateEventDto.getEventDate() != null) {
			checkEventDate(updateEventDto.getEventDate());
			event.setEventDate(updateEventDto.getEventDate());
		}
		if (updateEventDto.getPaid() != null)
			event.setPaid(updateEventDto.getPaid());
		if (updateEventDto.getParticipantLimit() != null)
			event.setParticipantLimit(updateEventDto.getParticipantLimit());
		if (updateEventDto.getTitle() != null)
			event.setTitle(updateEventDto.getTitle());
		if (updateEventDto.getStateAction() != null) {
			switch (updateEventDto.getStateAction()) {
				case PUBLISH_EVENT:
					event.setState(EventState.PUBLISHED);
					event.setPublishedOn(LocalDateTime.now());
					break;
				case CANCEL_REVIEW:
					event.setState(EventState.CANCELED);
					break;
				case SEND_TO_REVIEW:
					event.setState(EventState.PENDING);
					break;
				case REJECT_EVENT:
					event.setState(EventState.CANCELED);
					break;
			}
		}
		return event;
	}

	private void checkEventDate(LocalDateTime dateTime) {
		if (dateTime.isBefore(LocalDateTime.now().plusHours(1)))
			throw new ConflictException("Дата начала события меньше чем час " + dateTime);
	}

	private UserDto getUser(Long userId) {
		Optional<UserDto> user = Optional.ofNullable(userClient.getUser(userId));
		if (user.isEmpty()) {
			throw new NotFoundException("Пользователь не найден");
		}
		return user.get();
	}

	private Category getCategory(Long categoryId) {
		Optional<Category> category = categoryRepository.findById(categoryId);
		if (category.isEmpty()) {
			throw new NotFoundException("Категория не найдена");
		}
		return category.get();
	}


	private void updateEventFields(UpdateEventDto eventDto, Event foundEvent) {
		if (eventDto.getCategory() != null) {
			Category category = getCategory(eventDto.getCategory());
			foundEvent.setCategory(category);
		}

		if (eventDto.getAnnotation() != null && !eventDto.getAnnotation().isBlank()) {
			foundEvent.setAnnotation(eventDto.getAnnotation());
		}
		if (eventDto.getDescription() != null && !eventDto.getDescription().isBlank()) {
			foundEvent.setDescription(eventDto.getDescription());
		}
		if (eventDto.getEventDate() != null) {
			if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
				throw new ConflictException("Дата начала события не может быть раньше чем через 2 часа");
			}
			foundEvent.setEventDate(eventDto.getEventDate());
		}
		if (eventDto.getPaid() != null) {
			foundEvent.setPaid(eventDto.getPaid());
		}
		if (eventDto.getParticipantLimit() != null) {
			if (eventDto.getParticipantLimit() < 0) {
				throw new ValidationException("Participant limit cannot be negative");
			}
			foundEvent.setParticipantLimit(eventDto.getParticipantLimit());
		}
		if (eventDto.getRequestModeration() != null) {
			foundEvent.setRequestModeration(eventDto.getRequestModeration());
		}
		if (eventDto.getTitle() != null && !eventDto.getTitle().isBlank()) {
			foundEvent.setTitle(eventDto.getTitle());
		}
		if (eventDto.getLocation() != null) {
			if (eventDto.getLocation().getLat() != null) {
				foundEvent.setLat(eventDto.getLocation().getLat());
			}
			if (eventDto.getLocation().getLon() != null) {
				foundEvent.setLon(eventDto.getLocation().getLon());
			}
		}

		if (eventDto.getStateAction() != null) {
			switch (eventDto.getStateAction()) {
				case CANCEL_REVIEW -> foundEvent.setState(EventState.CANCELED);
				case PUBLISH_EVENT -> foundEvent.setState(EventState.PUBLISHED);
				case SEND_TO_REVIEW -> foundEvent.setState(EventState.PENDING);
			}
		}
	}


	private List<String> getListOfUri(List<Event> events, String uri) {
		return events.stream().map(Event::getId).map(id -> getUriForEvent(uri, id))
				.collect(Collectors.toList());
	}

	private String getUriForEvent(String uri, Long eventId) {
		return uri + "/" + eventId;
	}

	private RequestDto changeStatus(RequestDto request, RequestStatus status) {
		request.setStatus(status);
		return request;
	}

	private void checkRequestsStatus(List<RequestDto> requests) {
		Optional<RequestDto> confirmedReq = requests.stream()
				.filter(request -> request.getStatus().equals(RequestStatus.CONFIRMED))
				.findFirst();
		if (confirmedReq.isPresent())
			throw new ConflictException("Нельзя отменить, уже принятую заявку.");
	}
}
