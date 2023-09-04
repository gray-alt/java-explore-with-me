package ru.practicum.eventRequest.dto;

import ru.practicum.eventRequest.model.EventRequest;
import ru.practicum.eventRequest.model.EventRequestUpdateResult;

import java.util.Collection;
import java.util.stream.Collectors;

public class EventRequestMapper {
    public static EventRequestDto mapToEventRequestDto(EventRequest eventRequest) {
        return EventRequestDto.builder()
                .created(eventRequest.getCreateDate())
                .event(eventRequest.getEvent().getId())
                .id(eventRequest.getId())
                .requester(eventRequest.getRequester().getId())
                .status(eventRequest.getStatus())
                .build();
    }

    public static Collection<EventRequestDto> mapToEventRequestDto(Collection<EventRequest> requests) {
        return requests.stream().map(EventRequestMapper::mapToEventRequestDto).collect(Collectors.toList());
    }

    public static EventRequestStatusUpdateResultDto mapToEventRequestStatusUpdateResultDto(
            EventRequestUpdateResult requestUpdateResult) {
        Collection<EventRequestDto> confirmedResult = requestUpdateResult.getConfirmedRequests().stream().map(
                EventRequestMapper::mapToEventRequestDto).collect(Collectors.toList());
        Collection<EventRequestDto> rejectedResult = requestUpdateResult.getRejectedRequests().stream().map(
                EventRequestMapper::mapToEventRequestDto).collect(Collectors.toList());
        return new EventRequestStatusUpdateResultDto(confirmedResult, rejectedResult);
    }
}
