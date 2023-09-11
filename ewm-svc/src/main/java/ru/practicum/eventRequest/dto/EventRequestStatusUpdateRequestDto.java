package ru.practicum.eventRequest.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.eventRequest.model.EventRequestStatus;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public class EventRequestStatusUpdateRequestDto {
    private final Set<Long> requestIds;
    private final EventRequestStatus status;

    @Override
    public String toString() {
        return "EventRequestStatusUpdateRequestDto{" +
                "requestIds=" + requestIds +
                ", status=" + status +
                '}';
    }
}
