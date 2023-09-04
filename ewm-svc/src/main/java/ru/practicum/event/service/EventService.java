package ru.practicum.event.service;

import ru.practicum.event.model.Event;
import ru.practicum.event.model.GetEventRequest;

import java.util.Collection;

public interface EventService {
    Event addEvent(Long userId, Event event);

    Collection<Event> getAllEventsByUserId(Long userId, int from, int size);

    Event getEventByUserIdAndId(Long userId, Long eventId);

    Event updateEvent(Long userId, Long eventId, Event event);

    Event updateEventByAdmin(Long eventId, Event event);

    Collection<Event> getEventsByConditions(GetEventRequest request);

    Event getPublicEventById(Long eventId);
}
