package ru.practicum.location.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.practicum.location.model.Location;

import javax.persistence.TypedQuery;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LocationRepositoryTest {
    private final TestEntityManager em;
    private final LocationRepository locationRepository;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(em);
    }

    @Test
    void verifyRepositoryByPersistingLocation() {
        Location newLocation = Location.builder()
                .name("Test location")
                .lat(15.50D)
                .lon(15.50D)
                .radius(50D)
                .build();

        newLocation = locationRepository.save(newLocation);
        Assertions.assertNotNull(newLocation);

        TypedQuery<Location> query = em.getEntityManager().createQuery(
                "select loc from Location loc where loc.id = :id ",
                Location.class);

        Location foundLocation = query.setParameter("id", newLocation.getId()).getSingleResult();

        assertThat(foundLocation, allOf(
                hasProperty("id", equalTo(newLocation.getId())),
                hasProperty("name", equalTo(newLocation.getName())),
                hasProperty("lat", equalTo(newLocation.getLat())),
                hasProperty("lon", equalTo(newLocation.getLon())),
                hasProperty("radius", equalTo(newLocation.getRadius()))
        ));
    }

    @Test
    void findAllInRadius() {
        Location newLocationOne = Location.builder()
                .name("Test location")
                .lat(15.50D)
                .lon(15.50D)
                .radius(50D)
                .build();

        newLocationOne = locationRepository.save(newLocationOne);
        Assertions.assertNotNull(newLocationOne);

        Location newLocationTwo = Location.builder()
                .name("Test location")
                .lat(15.51D)
                .lon(15.51D)
                .radius(50D)
                .build();

        newLocationTwo = locationRepository.save(newLocationTwo);
        Assertions.assertNotNull(newLocationTwo);

        Location newLocationThree = Location.builder()
                .name("Test location")
                .lat(50.51D)
                .lon(50.51D)
                .radius(50D)
                .build();

        newLocationThree = locationRepository.save(newLocationThree);
        Assertions.assertNotNull(newLocationThree);

        Collection<Location> foundLocations = locationRepository.findAllInRadius(15.50D, 15.50D, 2000D);

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
    void findAllInCoordinates() {
        Location newLocationOne = Location.builder()
                .name("Test location")
                .lat(15.50D)
                .lon(15.50D)
                .radius(50D)
                .build();

        newLocationOne = locationRepository.save(newLocationOne);
        Assertions.assertNotNull(newLocationOne);

        Location newLocationTwo = Location.builder()
                .name("Test location")
                .lat(15.51D)
                .lon(15.51D)
                .radius(50D)
                .build();

        newLocationTwo = locationRepository.save(newLocationTwo);
        Assertions.assertNotNull(newLocationTwo);

        Location newLocationThree = Location.builder()
                .name("Test location")
                .lat(50.51D)
                .lon(50.51D)
                .radius(0D)
                .build();

        newLocationThree = locationRepository.save(newLocationThree);
        Assertions.assertNotNull(newLocationThree);

        Collection<Location> foundLocations = locationRepository.findAllInCoordinates(15.50D, 15.50D);

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
}
