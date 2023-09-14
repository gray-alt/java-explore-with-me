package ru.practicum.location.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.location.model.Location;

import java.util.Collection;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLon(Double lat, Double lon);

    boolean existsByLatAndLon(Double lat, Double lon);

    boolean existsByLatAndLonAndIdNot(Double lat, Double lon, Long id);

    @Query(value = "select loc " +
            "from Location loc " +
            "where distance(loc.lat, loc.lon, :lat, :lon) <= :radius")
    Collection<Location> findAllInRadius(@Param("lat") Double lat,
                                         @Param("lon") Double lon,
                                         @Param("radius") Double radius);

    @Query(value = "select loc " +
            "from Location loc " +
            "where distance(loc.lat, loc.lon, :lat, :lon) <= loc.radius")
    Collection<Location> findAllInCoordinates(@Param("lat") Double lat, @Param("lon") Double lon);
}
