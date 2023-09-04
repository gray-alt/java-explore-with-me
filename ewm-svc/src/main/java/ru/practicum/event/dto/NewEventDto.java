package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.location.dto.LocationDto;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class NewEventDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    private final String annotation;
    @NotNull
    private final long category;
    @NotBlank
    @Length(min = 20, max = 7000)
    private final String description;
    @NotNull
    @Future
    private final LocalDateTime eventDate;
    @NotNull
    private final LocationDto location;
    @NotNull
    private final Boolean paid;
    @NotNull
    private final Integer participantLimit;
    @NotNull
    private final boolean requestModeration;
    @NotBlank
    @Length(min = 3, max = 120)
    private final String title;
}
