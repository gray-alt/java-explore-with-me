package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.model.Stats(" +
            "   h.app, " +
            "   h.uri, " +
            "   case " +
            "       when ?4 = true " +
            "           then count (distinct h.ip) " +
            "       else count(h.ip) " +
            "   end as hits) " +
            "from Hit as h " +
            "where h.created between ?1 and ?2 " +
            "   and (?3 is null or h.uri in (?3))" +
            "group by " +
            "   h.app," +
            "   h.uri")
    List<Stats> findAllByCreatedBetweenAndUriInAndUnique(LocalDateTime start, LocalDateTime end, Collection<String> uris,
                                                         boolean unique);
}
