package ru.practicum.location.service;

import ru.practicum.location.model.Location;

public interface LocationService {
    Location addLocation(Location location);

    Location getLocation(Location location);
}
