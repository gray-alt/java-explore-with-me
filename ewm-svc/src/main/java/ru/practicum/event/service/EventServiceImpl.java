package ru.practicum.event.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.model.*;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.eventRequest.model.EventRequestStatus;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserService userService;
    private final CategoryService categoryService;

    @Override
    public Event addEvent(Long userId, Event event) {
        checkEventDate(event.getEventDate(), LocalDateTime.now().plusHours(2));
        event.setInitiator(userService.getUserById(userId));
        event.setCategory(categoryService.getCategoryById(event.getCategory().getId()));
        return eventRepository.save(event);
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
    public Event updateEvent(Long userId, Long eventId, Event event) {
        Event foundEvent = getEventByUserIdAndId(userId, eventId);

        // Изменять можно только не опубликованные события
        if (foundEvent.getState() == EventState.PUBLISHED) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }

        // Дата события должна быть не ранее чем за два часа до изменения
        checkEventDate(event.getEventDate(), LocalDateTime.now().plusHours(2));

        updateEvent(foundEvent, event);
        return eventRepository.save(foundEvent);
    }

    @Override
    public Event updateEventByAdmin(Long eventId, Event event) {
        Event foundEvent = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found"));

        // Дата события должна быть не ранее чем за час до публикации
        checkEventDate(foundEvent.getEventDate(), LocalDateTime.now().plusHours(1));

        // Публикация события не в статусе Ожидания публикации
        if (event.getState() == EventState.PUBLISHED && foundEvent.getState() != EventState.SEND_TO_REVIEW) {
            throw new ForbiddenException("Cannot publish the event because it's not in the right state: " +
                    foundEvent.getState());
        }

        // Отклонить можно событие, которе ещё не опубликовано
        if (event.getState() == EventState.CANCEL_REVIEW && foundEvent.getState() == EventState.PUBLISHED) {
            throw new ForbiddenException("Cannot cancel the event because it's not in the right state: " +
                    foundEvent.getState());
        }

        updateEvent(foundEvent, event);
        return eventRepository.save(foundEvent);
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
            foundEvent.setEventDate(event.getEventDate());
        }
        if (event.getLocation() != null) {
            foundEvent.setLocation(event.getLocation());
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
        if (event.getState() != null) {
            foundEvent.setState(event.getState());
        }
        if (event.getTitle() != null) {
            foundEvent.setTitle(event.getTitle());
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
        if (request.getAvailable() != null) {
            conditions.add(event.eventRequests.any().eq(EventRequestStatus.CONFIRMED).count().lt(event.participantLimit));
        }

        BooleanExpression finalCondition = conditions.stream().reduce(BooleanExpression::and).get();

        int page = request.getFrom() > 0 ? request.getFrom() / request.getSize() : 0;
        Sort sort = makePageSort(request.getSort());
        PageRequest pageRequest = PageRequest.of(page, request.getSize(), sort);

        return eventRepository.findAll(finalCondition, pageRequest).toList();
    }

    private BooleanExpression makeEventDateCondition(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        if (rangeStart != null && rangeEnd != null) {
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
        if (Objects.requireNonNull(sort) == EventSort.VIEWS) {
            return Sort.by("views").descending();
        }
        return Sort.by("eventDate").descending();
    }

    @Override
    public Event getPublicEventById(Long eventId) {
        return eventRepository.findByIdAndState(eventId, EventState.PUBLISHED).orElseThrow(
                () -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }
}
