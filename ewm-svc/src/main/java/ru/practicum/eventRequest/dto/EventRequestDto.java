package ru.practicum.eventRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.eventRequest.model.EventRequestStatus;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
public class EventRequestDto {
    private final LocalDateTime created;
    private final Long event;
    private final Long id;
    private final Long requester;
    private final EventRequestStatus status;
}
