package ru.practicum.event.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Getter
@Builder
public class EventShortDto {
    private final String annotation;
    private final CategoryDto category;
    private final long confirmedRequests;
    private final LocalDateTime eventDate;
    private final Long id;
    private final UserShortDto initiator;
    private final Boolean paid;
    private final String title;
    private final int views;
}
