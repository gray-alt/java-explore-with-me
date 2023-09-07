package ru.practicum.eventRequest.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.eventRequest.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.eventRequest.model.EventRequest;
import ru.practicum.eventRequest.model.EventRequestStatus;
import ru.practicum.eventRequest.model.EventRequestUpdateResult;
import ru.practicum.eventRequest.repository.EventRequestRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EventRequestServiceImpl implements EventRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final EventService eventService;
    private final UserService userService;

    @Override
    public EventRequest addEventRequest(Long userId, Long eventId) {
        if (eventRequestRepository.existsByEventIdAndRequesterId(eventId, userId)) {
            throw new ConflictException("Can't add repeated request");
        }

        Event event = eventService.getEventById(eventId);
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("Initiator can't make request");
        }

        if (event.getState() != EventState.PUBLISHED) {
            throw new ConflictException("Event with id=" + eventId + " is not published yet");
        }

        if (event.getParticipantLimit() != 0 && event.getConfirmedRequestsCount() == event.getParticipantLimit()) {
            throw new ConflictException("The limit of participants has been reached");
        }

        User user = userService.getUserById(userId);
        EventRequestStatus status = EventRequestStatus.PENDING;

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            status = EventRequestStatus.CONFIRMED;
        }

        EventRequest eventRequest = EventRequest.builder()
                .createDate(LocalDateTime.now())
                .event(event)
                .requester(user)
                .status(status)
                .build();

        return eventRequestRepository.save(eventRequest);
    }

    @Override
    public Collection<EventRequest> getAllEventRequests(Long userId) {
        return eventRequestRepository.findAllByRequesterId(userId);
    }

    @Override
    public EventRequest cancelEventRequest(Long userId, Long requestId) {
        EventRequest request = eventRequestRepository.findByIdAndRequesterId(requestId, userId).orElseThrow(
                () -> new NotFoundException("Request with id=" + requestId + " was not found"));
        request.setStatus(EventRequestStatus.CANCELED);
        return eventRequestRepository.save(request);
    }

    @Override
    public Collection<EventRequest> getEventRequestsByUserIdAndEventId(Long userId, Long eventId) {
        return eventRequestRepository.findAllByEventIdAndEventInitiatorId(eventId, userId);
    }

    @Override
    public EventRequestUpdateResult confirmEventRequests(Long userId, Long eventId,
                                                         EventRequestStatusUpdateRequestDto request) {
        Event foundEvent = eventService.getPublicEventById(eventId, null);

        Set<Long> exodusIds = request.getRequestIds();
        Collection<EventRequest> eventRequests = eventRequestRepository.findAllByEventIdAndEventInitiatorIdAndIdIn(
                eventId, userId, request.getRequestIds());
        if (eventRequests.size() < exodusIds.size()) {
            Set<Long> eventIds = eventRequests.stream().map(EventRequest::getId).collect(Collectors.toSet());
            exodusIds.retainAll(eventIds);
            throw new NotFoundException("Event requests with id=" + exodusIds + " was not found");
        }

        if (request.getStatus() == EventRequestStatus.CONFIRMED) {
            if (foundEvent.getParticipantLimit() == 0 || !foundEvent.getRequestModeration()) {
                return new EventRequestUpdateResult(eventRequests, new ArrayList<>());
            }

            if (foundEvent.getParticipantLimit() == foundEvent.getConfirmedRequestsCount()) {
                throw new ConflictException("The participant limit has been reached");
            }
        }

        long confirmedRequestsCount = foundEvent.getConfirmedRequestsCount();

        EventRequestStatus newStatus;
        ArrayList<EventRequest> confirmedRequests = new ArrayList<>();
        ArrayList<EventRequest> rejectedRequests = new ArrayList<>();

        for (EventRequest e : eventRequests) {
            if (request.getStatus() == EventRequestStatus.CONFIRMED &&
                    foundEvent.getParticipantLimit() == confirmedRequestsCount) {
                newStatus = EventRequestStatus.REJECTED;
            } else {
                newStatus = request.getStatus();
            }

            if (e.getStatus() != EventRequestStatus.PENDING) {
                throw new ConflictException("Request must have status PENDING");
            }

            e.setStatus(newStatus);

            if (newStatus == EventRequestStatus.CONFIRMED) {
                confirmedRequestsCount++;
                confirmedRequests.add(eventRequestRepository.save(e));
            } else {
                rejectedRequests.add(eventRequestRepository.save(e));
            }
        }

        return new EventRequestUpdateResult(confirmedRequests, rejectedRequests);
    }
}
