package ru.practicum.location.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class LocationDto {
    private final Double lat;
    private final Double lon;
}
