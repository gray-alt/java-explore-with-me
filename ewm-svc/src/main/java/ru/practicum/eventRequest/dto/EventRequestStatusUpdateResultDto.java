package ru.practicum.eventRequest.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
public class EventRequestStatusUpdateResultDto {
    private final Collection<EventRequestDto> confirmedRequests;
    private final Collection<EventRequestDto> rejectedRequests;
}
