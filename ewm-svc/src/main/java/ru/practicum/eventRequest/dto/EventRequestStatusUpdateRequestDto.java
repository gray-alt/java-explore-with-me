package ru.practicum.eventRequest.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.practicum.eventRequest.model.EventRequestStatus;

import java.util.Set;

@AllArgsConstructor
@Getter
public class EventRequestStatusUpdateRequestDto {
    private final Set<Long> requestIds;
    private final EventRequestStatus status;
}
