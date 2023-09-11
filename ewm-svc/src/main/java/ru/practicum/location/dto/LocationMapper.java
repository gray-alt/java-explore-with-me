package ru.practicum.location.dto;

import org.springframework.stereotype.Service;
import ru.practicum.location.model.Location;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class LocationMapper {
    public static Location mapToLocation(LocationDto locationDto) {
        if (locationDto == null) {
            return null;
        }
        return Location.builder()
                .lon(locationDto.getLon())
                .lat(locationDto.getLat())
                .name(locationDto.getName())
                .radius(locationDto.getRadius() == null ? 0 : locationDto.getRadius())
                .myLocation(locationDto.isMyLocation())
                .build();
    }

    public static LocationDto mapToLocationDto(Location location) {
        return LocationDto.builder()
                .id(location.getId())
                .lon(location.getLon())
                .lat(location.getLat())
                .name(location.getName())
                .radius(location.getRadius())
                .build();
    }

    public static Collection<LocationDto> mapToLocationDto(Collection<Location> locations) {
        return locations.stream().map(LocationMapper::mapToLocationDto).collect(Collectors.toList());
    }
}
