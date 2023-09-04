package ru.practicum.eventRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collection;

@AllArgsConstructor
@Getter
public class EventRequestStatusUpdateResultDto {
    private final Collection<EventRequestDto> confirmedRequests;
    private final Collection<EventRequestDto> rejectedRequests;
}
