package ru.practicum.location.dto;

import org.springframework.stereotype.Service;
import ru.practicum.location.model.Location;

@Service
public class LocationMapper {
    public static Location mapToLocation(LocationDto locationDto) {
        if (locationDto == null) {
            return null;
        }
        return Location.builder()
                .lon(locationDto.getLon())
                .lat(locationDto.getLat())
                .build();
    }

    public static LocationDto mapToLocationDto(Location location) {
        return LocationDto.builder()
                .lon(location.getLon())
                .lat(location.getLat())
                .build();
    }
}
