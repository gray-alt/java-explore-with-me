package ru.practicum.eventRequest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.eventRequest.model.EventRequestStatus;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Builder
public class EventRequestDto {
    private final LocalDateTime created;
    private final Long event;
    private final Long id;
    private final Long requester;
    private final EventRequestStatus status;
}
