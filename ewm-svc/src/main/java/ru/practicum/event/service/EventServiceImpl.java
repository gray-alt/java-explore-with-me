package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.service.LocationService;
import ru.practicum.user.service.UserService;


import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final LocationService locationService;

    @Override
    public Event addEvent(Long userId, Event event) {
        checkEventDate(event.getEventDate(), LocalDateTime.now().plusHours(2));
        event.setInitiator(userService.getUserById(userId));
        event.setCategory(categoryService.getCategoryById(event.getCategory().getId()));
        event.setLocation(locationService.getLocationByCoordinates(event.getLocation()));
        event = eventRepository.save(event);
        log.info("Added event {} with id={}", event.getTitle(), event.getId());
        return event;
    }

    @Override
    public Collection<Event> getAllEventsByUserId(Long userId, int from, int size) {
        int page = from > 0 ? from / size : 0;
        PageRequest pageRequest = PageRequest.of(page, size);
        return eventRepository.findAllByInitiatorId(userId, pageRequest);
    }

    @Override
    public Event getEventByUserIdAndId(Long userId, Long eventId) {
        return eventRepository.findByInitiatorIdAndId(userId, eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    @Override
    public Event updateEventByInitiator(Long userId, Long eventId, Event event) {
        Event foundEvent = getEventByUserIdAndId(userId, eventId);

        // Изменять можно только не опубликованные события
        if (foundEvent.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Only pending or canceled events can be changed");
        }

        // Дата события должна быть не ранее чем за два часа до изменения
        checkEventDate(foundEvent.getEventDate(), LocalDateTime.now().plusHours(2));

        updateEvent(foundEvent, event);
        foundEvent = eventRepository.save(foundEvent);
        log.info("Updated event with id={}", eventId);
        return foundEvent;
    }

    @Override
    public Event updateEventByAdmin(Long eventId, Event event) {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found"));

        // Дата события должна быть не ранее чем за час до публикации
        checkEventDate(foundEvent.getEventDate(), LocalDateTime.now().plusHours(1));

        // Публикация события не в статусе Ожидания публикации
        if (event.getStateAction() == EventStateAction.PUBLISH_EVENT && foundEvent.getState() != EventState.PENDING) {
            throw new ConflictException("Cannot publish the event because it's not in the right state: " +
                    foundEvent.getState());
        }

        // Отклонить можно событие, которе ещё не опубликовано
        if (event.getStateAction() == EventStateAction.REJECT_EVENT && foundEvent.getState() == EventState.PUBLISHED) {
            throw new ConflictException("Cannot cancel the event because it's not in the right state: " +
                    foundEvent.getState());
        }

        updateEvent(foundEvent, event);
        foundEvent = eventRepository.save(foundEvent);
        log.info("Admin updated event with id={}", eventId);
        return foundEvent;
    }

    private void checkEventDate(LocalDateTime eventDate, LocalDateTime validDateTime) {
        if (eventDate == null || eventDate.isBefore(validDateTime)) {
            throw new ForbiddenException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. " +
                    "Value: " + eventDate);
        }
    }

    private void updateEvent(Event foundEvent, Event event) {
        if (event.getAnnotation() != null) {
            foundEvent.setAnnotation(event.getAnnotation());
        }
        if (event.getCategory() != null) {
            foundEvent.setCategory(categoryService.getCategoryById(event.getCategory().getId()));
        }
        if (event.getDescription() != null) {
            foundEvent.setDescription(event.getDescription());
        }
        if (event.getEventDate() != null) {
            // Новая дата должна быть в будущем
            checkEventDate(event.getEventDate(), LocalDateTime.now().plusHours(2));
            foundEvent.setEventDate(event.getEventDate());
        }
        if (event.getLocation() != null) {
            foundEvent.setLocation(locationService.getLocationByCoordinates(event.getLocation()));
        }
        if (event.getPaid() != null) {
            foundEvent.setPaid(event.getPaid());
        }
        if (event.getParticipantLimit() != null) {
            foundEvent.setParticipantLimit(event.getParticipantLimit());
        }
        if (event.getRequestModeration() != null) {
            foundEvent.setRequestModeration(event.getRequestModeration());
        }
        if (event.getTitle() != null) {
            foundEvent.setTitle(event.getTitle());
        }
        foundEvent.setState(getEvenStateByEventStateAction(event.getStateAction(), foundEvent.getState()));
    }

    private EventState getEvenStateByEventStateAction(EventStateAction stateAction, EventState defaultState) {
        if (stateAction == null) {
            return defaultState;
        }
        switch (stateAction) {
            case REJECT_EVENT:
            case CANCEL_REVIEW:
                return EventState.CANCELED;
            case PUBLISH_EVENT: return EventState.PUBLISHED;
            case SEND_TO_REVIEW: return EventState.PENDING;
            default: return defaultState;
        }
    }

    @Override
    public List<Event> getEventsByConditions(GetEventRequest request) {
        QEvent event = QEvent.event;

        List<BooleanExpression> conditions = new ArrayList<>();

        if (request.hasUserIds()) {
            conditions.add(event.initiator.id.in(request.getUserIds()));
        }
        if (request.hasStates()) {
            conditions.add(event.state.in(request.getStates()));
        }
        if (request.hasCategoryIds()) {
            conditions.add(event.category.id.in(request.getCategoryIds()));
        }
        if (request.hasRange()) {
            conditions.add(makeEventDateCondition(request.getRangeStart(), request.getRangeEnd()));
        }
        if (request.getText() != null) {
            conditions.add(makeEventTextSearchCondition(request.getText()));
        }
        if (request.getPaid() != null) {
            conditions.add(event.paid.eq(request.getPaid()));
        }
        if (request.getAvailable() != null && request.getAvailable()) {
            conditions.add(event.eventRequests.any().eq("CONFIRMED").count().lt(event.participantLimit));
        }
        if (request.hasLocationIds()) {
            conditions.add(event.location.id.in(request.getLocationIds()));
        }
        if (request.getSearchLocation() != null) {
            conditions.add(event.location.in(locationService.getLocationsInCoordinates(request.getSearchLocation())));
        }

        int page = request.getFrom() > 0 ? request.getFrom() / request.getSize() : 0;
        Sort sort = makePageSort(request.getSort());
        PageRequest pageRequest = PageRequest.of(page, request.getSize(), sort);

        List<Event> events;
        if (conditions.isEmpty()) {
             events = eventRepository.findAll(pageRequest).toList();
        } else {
            BooleanExpression finalCondition = conditions.stream().reduce(BooleanExpression::and).get();
            events = eventRepository.findAll(finalCondition, pageRequest).toList();
        }
        for (Event e : events) {
            updateEventViews(e, request.getIp());
        }
        return events;
    }

    private BooleanExpression makeEventDateCondition(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.isAfter(rangeEnd)) {
                throw new BadRequestException("Incorrect range Start=" + rangeStart + ", End=" + rangeEnd);
            }
            return QEvent.event.eventDate.between(rangeStart, rangeEnd);
        } else if (rangeStart != null) {
            return QEvent.event.eventDate.goe(rangeStart);
        } else {
            return QEvent.event.eventDate.loe(rangeEnd);
        }
    }

    private BooleanExpression makeEventTextSearchCondition(String text) {
        return QEvent.event.description.containsIgnoreCase(text).or(
                QEvent.event.annotation.containsIgnoreCase(text));
    }

    private Sort makePageSort(EventSort sort) {
        if (sort == null || sort == EventSort.VIEWS) {
            return Sort.by("views").descending();
        }
        return Sort.by("eventDate").descending();
    }

    @Override
    public Event getPublicEventById(Long eventId, String ip) {
        Event foundEvent = eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found"));
        return updateEventViews(foundEvent, ip);
    }

    private Event updateEventViews(Event event, String ip) {
        if (ip == null) {
            return event;
        }
        Set<String> ipViews = event.getIpViews();
        if (ipViews == null) {
            ipViews = new HashSet<>();
        }
        ipViews.add(ip);
        event.setIpViews(ipViews);
        return eventRepository.save(event);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }
}
