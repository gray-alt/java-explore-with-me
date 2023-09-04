package ru.practicum.location.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class LocationDto {
    private final Double lat;
    private final Double lon;
}
