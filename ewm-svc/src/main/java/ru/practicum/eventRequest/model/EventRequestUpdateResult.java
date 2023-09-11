package ru.practicum.eventRequest.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@Getter
@Setter
public class EventRequestUpdateResult {
    private Collection<EventRequest> confirmedRequests;
    private Collection<EventRequest> rejectedRequests;
}
