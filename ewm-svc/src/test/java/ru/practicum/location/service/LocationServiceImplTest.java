package ru.practicum.location.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.location.model.Location;

import javax.transaction.Transactional;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LocationServiceImplTest {
    private final LocationService locationService;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    void addAlreadyExistsLocation() {
        Location tempLocation = makeLocation("Test location", 15.50D, 15.50D, 0D, false);
        Location newLocationOne = locationService.addLocation(tempLocation);
        Assertions.assertNotNull(newLocationOne);

        ConflictException e = Assertions.assertThrows(ConflictException.class,
                () -> locationService.addLocation(tempLocation));
        assertThat(e.getMessage(), startsWithIgnoringCase(
                "Location already exist with lat"));
    }

    @Test
    void addLocationWithoutName() {
        Location tempLocation = makeLocation(null, 16.50D, 16.50D, 0D, false);
        Location newLocationOne = locationService.addLocation(tempLocation);
        Assertions.assertNotNull(newLocationOne);

        assertThat(newLocationOne.getName(), startsWithIgnoringCase(
                "lat=" + newLocationOne.getLat() + ", lon=" + newLocationOne.getLon() +
                        ", radius=" + newLocationOne.getRadius()));
    }

    @Test
    void addLocation() {
        Location tempLocation = makeLocation("Test location", 15.50D, 15.50D, 0D, false);
        Location newLocationOne = locationService.addLocation(tempLocation);

        assertThat(newLocationOne, allOf(
                hasProperty("name", equalTo(tempLocation.getName())),
                hasProperty("lat", equalTo(tempLocation.getLat())),
                hasProperty("lon", equalTo(tempLocation.getLon())),
                hasProperty("radius", equalTo(tempLocation.getRadius()))
        ));
    }

    @Test
    void updateWrongLocation() {
        Location tempLocation = makeLocation("Test location", 15.50D, 15.50D, 0D, false);

        NotFoundException e = Assertions.assertThrows(NotFoundException.class,
                () -> locationService.updateLocation(999L, tempLocation));
        assertThat(e.getMessage(), startsWithIgnoringCase(
                "Not found location with id="));
    }

    @Test
    void updateLocationWithAlreadyExistsCoordinates() {
        Location tempLocation = makeLocation("Test location", 15.50D, 15.50D, 0D, false);
        Location tempLocationTwo = makeLocation("Test location", 16.50D, 16.50D, 0D, false);

        Location newLocationOne = locationService.addLocation(tempLocation);
        Assertions.assertNotNull(newLocationOne);

        Location newLocationTwo = locationService.addLocation(tempLocationTwo);
        Assertions.assertNotNull(newLocationTwo);

        ConflictException e = Assertions.assertThrows(ConflictException.class,
                () -> locationService.updateLocation(newLocationTwo.getId(), tempLocation));
        assertThat(e.getMessage(), startsWithIgnoringCase(
                "Location already exist with lat="));
    }

    @Test
    void updateLocation() {
        Location tempLocation = makeLocation("Test location", 15.50D, 15.50D, 0D, false);
        Location newLocationOne = locationService.addLocation(tempLocation);

        Location tempLocationTwo = makeLocation("Test location", 15.50D, 15.50D, 0D, false);
        tempLocationTwo.setName("Updated location");
        Location updatedLocation = locationService.updateLocation(newLocationOne.getId(), tempLocationTwo);

        assertThat(updatedLocation, allOf(
                hasProperty("id", equalTo(newLocationOne.getId())),
                hasProperty("name", equalTo(updatedLocation.getName())),
                hasProperty("lat", equalTo(newLocationOne.getLat())),
                hasProperty("lon", equalTo(newLocationOne.getLon())),
                hasProperty("radius", equalTo(newLocationOne.getRadius()))
        ));
    }

    @Test
    void getLocationByCoordinates() {
        Location tempLocation = makeLocation(null, 15.50D, 15.50D, 0D, false);
        Location newLocationOne = locationService.getLocationByCoordinates(tempLocation);
        Assertions.assertNotNull(newLocationOne);

        assertThat(newLocationOne, allOf(
                hasProperty("name", startsWithIgnoringCase("lat=" + newLocationOne.getLat() +
                        ", lon=" + newLocationOne.getLon() +
                        ", radius=" + newLocationOne.getRadius())),
                hasProperty("lat", equalTo(15.50D)),
                hasProperty("lon", equalTo(15.50D)),
                hasProperty("radius", equalTo(0D))
        ));
    }

    @Test
    void getLocationByIdWithWrongId() {
        NotFoundException e = Assertions.assertThrows(NotFoundException.class,
                () -> locationService.getLocationById(999L));
        assertThat(e.getMessage(), startsWithIgnoringCase(
                "Not found location with id="));
    }

    @Test
    void getLocationById() {
        Location tempLocation = makeLocation(null, 15.50D, 15.50D, 0D, false);
        Location newLocationOne = locationService.getLocationByCoordinates(tempLocation);
        Assertions.assertNotNull(newLocationOne);

        Location foundLocation = locationService.getLocationById(newLocationOne.getId());

        assertThat(foundLocation, allOf(
                hasProperty("name", equalTo(newLocationOne.getName())),
                hasProperty("lat", equalTo(newLocationOne.getLat())),
                hasProperty("lon", equalTo(newLocationOne.getLon())),
                hasProperty("radius", equalTo(newLocationOne.getRadius()))
        ));
    }

    @Test
    void getAllLocations() {
        Location tempLocation = makeLocation(null, 15.50D, 15.50D, 0D, false);
        Location newLocationOne = locationService.getLocationByCoordinates(tempLocation);
        Assertions.assertNotNull(newLocationOne);

        Collection<Location> foundLocations = locationService.getAllLocations(0, 1000);

        assertThat(foundLocations.size(), equalTo(1));
        assertThat(foundLocations, hasItem(allOf(
                hasProperty("id", equalTo(newLocationOne.getId())),
                hasProperty("name", equalTo(newLocationOne.getName())),
                hasProperty("lat", equalTo(newLocationOne.getLat())),
                hasProperty("lon", equalTo(newLocationOne.getLon())),
                hasProperty("radius", equalTo(newLocationOne.getRadius()))
        )));
    }

    @Test
    void getLocationsInCoordinatesInRadius() {
        Location newLocationOne = makeLocation(null, 15.50D, 15.50D, 0D, false);
        newLocationOne = locationService.addLocation(newLocationOne);
        Assertions.assertNotNull(newLocationOne);

        Location newLocationTwo = makeLocation(null, 16.50D, 16.50D, 0D, false);
        newLocationTwo = locationService.addLocation(newLocationTwo);
        Assertions.assertNotNull(newLocationTwo);

        Location newLocationThree = makeLocation(null, 50.50D, 50.50D, 0D, false);
        newLocationThree = locationService.addLocation(newLocationThree);
        Assertions.assertNotNull(newLocationThree);

        Location locationForSearch = makeLocation(null, 15.50D, 15.50D, 2000D, false);

        Collection<Location> foundLocations = locationService.getLocationsInCoordinates(locationForSearch);

        assertThat(foundLocations.size(), equalTo(2));
        assertThat(foundLocations, hasItem(allOf(
                hasProperty("id", equalTo(newLocationOne.getId())),
                hasProperty("name", equalTo(newLocationOne.getName())),
                hasProperty("lat", equalTo(newLocationOne.getLat())),
                hasProperty("lon", equalTo(newLocationOne.getLon())),
                hasProperty("radius", equalTo(newLocationOne.getRadius()))
        )));
        assertThat(foundLocations, hasItem(allOf(
                hasProperty("id", equalTo(newLocationTwo.getId())),
                hasProperty("name", equalTo(newLocationTwo.getName())),
                hasProperty("lat", equalTo(newLocationTwo.getLat())),
                hasProperty("lon", equalTo(newLocationTwo.getLon())),
                hasProperty("radius", equalTo(newLocationTwo.getRadius()))
        )));
    }

    @Test
    void getLocationsInCoordinatesByMyCoordinates() {
        Location newLocationOne = makeLocation(null, 15.50D, 15.50D, 50D, false);
        newLocationOne = locationService.addLocation(newLocationOne);
        Assertions.assertNotNull(newLocationOne);

        Location newLocationTwo = makeLocation(null, 15.51D, 15.51D, 50D, false);
        newLocationTwo = locationService.addLocation(newLocationTwo);
        Assertions.assertNotNull(newLocationTwo);

        Location newLocationThree = makeLocation(null, 50.50D, 50.50D, 0D, false);
        newLocationThree = locationService.addLocation(newLocationThree);
        Assertions.assertNotNull(newLocationThree);

        Location locationForSearch = makeLocation(null, 15.50D, 15.50D, 0D, true);

        Collection<Location> foundLocations = locationService.getLocationsInCoordinates(locationForSearch);

        assertThat(foundLocations.size(), equalTo(2));
        assertThat(foundLocations, hasItem(allOf(
                hasProperty("id", equalTo(newLocationOne.getId())),
                hasProperty("name", equalTo(newLocationOne.getName())),
                hasProperty("lat", equalTo(newLocationOne.getLat())),
                hasProperty("lon", equalTo(newLocationOne.getLon())),
                hasProperty("radius", equalTo(newLocationOne.getRadius()))
        )));
        assertThat(foundLocations, hasItem(allOf(
                hasProperty("id", equalTo(newLocationTwo.getId())),
                hasProperty("name", equalTo(newLocationTwo.getName())),
                hasProperty("lat", equalTo(newLocationTwo.getLat())),
                hasProperty("lon", equalTo(newLocationTwo.getLon())),
                hasProperty("radius", equalTo(newLocationTwo.getRadius()))
        )));
    }

    private Location makeLocation(String name, Double lat, Double lon, Double radius, boolean itMyLocation) {
        return Location.builder()
                .name(name)
                .lat(lat)
                .lon(lon)
                .radius(radius)
                .itMyLocation(itMyLocation)
                .build();
    }
}
