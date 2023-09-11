package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.*;
import ru.practicum.event.service.EventService;
import ru.practicum.eventRequest.dto.EventRequestMapper;
import ru.practicum.eventRequest.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.eventRequest.dto.EventRequestDto;
import ru.practicum.eventRequest.dto.EventRequestStatusUpdateRequestDto;
import ru.practicum.eventRequest.service.EventRequestService;

import javax.validation.Valid;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
@Validated
public class PrivateController {
    private final EventService eventService;
    private final EventRequestService eventRequestService;

    // СОБЫТИЯ
    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                             @RequestBody @Valid NewEventDto newEventDto) {
        return EventMapper.mapToEventFullDto(
                eventService.addEvent(userId, EventMapper.mapToEvent(newEventDto)));
    }

    @GetMapping("/{userId}/events")
    public Collection<EventShortDto> getEvents(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return EventMapper.mapToEventShortDto(eventService.getAllEventsByUserId(userId, from, size));
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId, @PathVariable Long eventId) {
        return EventMapper.mapToEventFullDto(eventService.getEventByUserIdAndId(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @RequestBody @Valid UpdateEventDto updateEventDto) {
        return EventMapper.mapToEventFullDto(
                eventService.updateEventByInitiator(userId, eventId, EventMapper.mapToEvent(updateEventDto)));
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public Collection<EventRequestDto> getEventRequests(@PathVariable Long userId,
                                                        @PathVariable Long eventId) {
        return EventRequestMapper.mapToEventRequestDto(
                eventRequestService.getEventRequestsByUserIdAndEventId(userId, eventId));
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateEventRequestsStatus(@PathVariable Long userId,
                                                                       @PathVariable Long eventId,
                                                                       @RequestBody @Valid EventRequestStatusUpdateRequestDto requestStatusDto) {
        return EventRequestMapper.mapToEventRequestStatusUpdateResultDto(
                eventRequestService.confirmEventRequests(userId, eventId, requestStatusDto));
    }

    // ЗАПРОСЫ НА УЧАСТИЕ
    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public EventRequestDto addRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return EventRequestMapper.mapToEventRequestDto(eventRequestService.addEventRequest(userId, eventId));
    }

    @GetMapping("/{userId}/requests")
    public Collection<EventRequestDto> getRequests(@PathVariable Long userId) {
        return EventRequestMapper.mapToEventRequestDto(eventRequestService.getAllEventRequests(userId));
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public EventRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return EventRequestMapper.mapToEventRequestDto(eventRequestService.cancelEventRequest(userId, requestId));
    }
}
