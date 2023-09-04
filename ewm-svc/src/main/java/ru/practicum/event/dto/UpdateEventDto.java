package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.model.EventState;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.Future;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UpdateEventDto {
    @Length(min = 20, max = 2000)
    private final String annotation;
    private final long category;
    @Length(min = 20, max = 7000)
    private final String description;
    @Future
    private final LocalDateTime eventDate;
    private final LocationDto location;
    private final Boolean paid;
    private final Integer participantLimit;
    private final Boolean requestModeration;
    private final EventState stateAction;
    @Length(min = 3, max = 120)
    private final String title;
}
