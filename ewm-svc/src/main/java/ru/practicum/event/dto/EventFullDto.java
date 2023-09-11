package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.EventState;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Builder
public class EventFullDto {
    private final String annotation;
    private final CategoryDto category;
    private final long confirmedRequests;
    private final LocalDateTime createdOn;
    private final String description;
    private final LocalDateTime eventDate;
    private final Long id;
    private final UserShortDto initiator;
    private final LocationDto location;
    private final Boolean paid;
    private final Long participantLimit;
    private final LocalDateTime publishedOn;
    private final Boolean requestModeration;
    private final EventState state;
    private final String title;
    private final int views;
}
