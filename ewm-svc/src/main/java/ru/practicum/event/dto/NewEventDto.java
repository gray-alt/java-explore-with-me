package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
    private final Long category;
    @NotBlank
    @Length(min = 20, max = 7000)
    private final String description;
    @NotNull
    @Future
    private final LocalDateTime eventDate;
    @NotNull
    private final LocationDto location;
    private final boolean paid;
    private final long participantLimit;
    private final Boolean requestModeration;
    @NotBlank
    @Length(min = 3, max = 120)
    private final String title;

    @Override
    public String toString() {
        return "NewEventDto{" +
                "annotation='" + annotation + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", eventDate=" + eventDate +
                ", location=" + location +
                ", paid=" + paid +
                ", participantLimit=" + participantLimit +
                ", requestModeration=" + requestModeration +
                ", title='" + title + '\'' +
                '}';
    }
}