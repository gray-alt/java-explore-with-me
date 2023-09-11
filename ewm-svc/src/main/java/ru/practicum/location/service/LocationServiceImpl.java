package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;

@RequiredArgsConstructor
@Slf4j
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Location addLocation(Location location) {
        location = locationRepository.save(location);
        log.info("Added location with id={}", location.getId());
        return location;
    }

    @Override
    public Location getLocation(Location location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon()).orElse(
                locationRepository.save(location));
    }
}
