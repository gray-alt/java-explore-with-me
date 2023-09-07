package ru.practicum.location.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;

@AllArgsConstructor
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location getLocation(Location location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon()).orElse(
                locationRepository.save(location));
    }
}
