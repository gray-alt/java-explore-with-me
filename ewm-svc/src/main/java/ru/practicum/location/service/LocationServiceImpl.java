package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.model.Location;
import ru.practicum.location.repository.LocationRepository;

import java.util.Collection;

@RequiredArgsConstructor
@Slf4j
@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Override
    public Location addLocation(Location location) {
        if (locationRepository.existsByLatAndLon(location.getLat(), location.getLon())) {
            throw new ConflictException("Location already exist with lat=" + location.getLat() +
                    " and lon=" + location.getLon());
        }
        if (location.getName() == null) {
            location.setName("lat=" + location.getLat() + ", lon=" + location.getLon() +
                    ", radius=" + location.getRadius());
        }
        location = locationRepository.save(location);
        log.info("Added location {} with id={}", location.getName(), location.getId());
        return location;
    }

    @Override
    public Location updateLocation(Long locId, Location location) {
        Location foundLocation = locationRepository.findById(locId).orElseThrow(
                () -> new NotFoundException("Not found location with id=" + locId));
        if (location.getLat() != null) {
            foundLocation.setLat(location.getLat());
        }
        if (location.getLon() != null) {
            foundLocation.setLon(location.getLon());
        }
        if (locationRepository.existsByLatAndLonAndIdNot(foundLocation.getLat(), foundLocation.getLon(), locId)) {
            throw new ConflictException("Location already exist with lat=" + location.getLat() +
                    " and lon=" + location.getLon());
        }
        if (location.getName() != null) {
            foundLocation.setName(location.getName());
        }
        if (location.getRadius() != null) {
            foundLocation.setRadius(location.getRadius());
        }
        foundLocation = locationRepository.save(foundLocation);
        log.info("Updated location with id={}", locId);
        return foundLocation;
    }

    @Override
    public Location getLocationByCoordinates(Location location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon()).orElse(
                addLocation(location));
    }

    @Override
    public Location getLocationById(Long locId) {
        return locationRepository.findById(locId).orElseThrow(
                () -> new NotFoundException("Not found location with id=" + locId));
    }

    @Override
    public Collection<Location> getAllLocations(int from, int size) {
        int page = from > 0 ? from / size : 0;
        PageRequest pageRequest = PageRequest.of(page, size);
        return locationRepository.findAll(pageRequest).toList();
    }

    @Override
    public Collection<Location> getLocationsInRadius(Location location) {
        return locationRepository.findAllInRadius(location.getLat(), location.getLon(), location.getRadius());
    }

    @Override
    public Collection<Location> getLocationsInCoordinates(Location location) {
        return locationRepository.findAllInCoordinates(location.getLat(), location.getLon());
    }
}
