package ru.practicum.location.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Getter
@Builder
public class LocationDto {
    private final Long id;
    @NotNull
    private final Double lat;
    @NotNull
    private final Double lon;
    @NotNull
    @Length(min = 3, max = 255)
    private final String name;
    private final Double radius;
    private final boolean myLocation;
}
