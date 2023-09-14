package ru.practicum.location.service;

import ru.practicum.location.model.Location;

import java.util.Collection;

public interface LocationService {
    Location addLocation(Location location);

    Location updateLocation(Long locId, Location location);

    Location getLocationByCoordinates(Location location);

    Collection<Location> getLocationsInCoordinates(Location location);

    Location getLocationById(Long locId);

    Collection<Location> getAllLocations(int from, int size);
}
