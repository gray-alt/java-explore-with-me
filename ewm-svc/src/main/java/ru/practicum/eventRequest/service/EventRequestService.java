package ru.practicum.eventRequest.service;

import ru.practicum.eventRequest.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.eventRequest.model.EventRequest;
import ru.practicum.eventRequest.model.EventRequestUpdateResult;

import java.util.Collection;

public interface EventRequestService {
    EventRequest addEventRequest(Long userId, Long eventId);

    Collection<EventRequest> getAllEventRequests(Long userId);

    EventRequest cancelEventRequest(Long userId, Long requestId);

    Collection<EventRequest> getEventRequestsByUserIdAndEventId(Long userId, Long eventId);

    EventRequestUpdateResult confirmEventRequests(Long userId, Long eventId, EventRequestStatusUpdateRequestDto request);
}
