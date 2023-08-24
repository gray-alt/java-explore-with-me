package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.model.Stat(" +
            "   h.app, " +
            "   h.uri, " +
            "   count(h) as hits)" +
            "from Hit as h " +
            "group by " +
            "   h.app," +
            "   h.uri")
    List<Stat> findAllByCreatedBetweenAndUriInAndUnique(LocalDateTime start, LocalDateTime end, Collection<String> uris,
                                                        boolean unique);
}
