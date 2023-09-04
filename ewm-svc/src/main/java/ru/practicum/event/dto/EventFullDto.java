package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@AllArgsConstructor
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
    private final Integer participantLimit;
    private final LocalDateTime publishedOn;
    private final Boolean requestModeration;
    private final String title;
    private final int views;
}
