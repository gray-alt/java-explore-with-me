package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query("select new ru.practicum.model.Stats(" +
            "   h.app, " +
            "   h.uri, " +
            "   case " +
            "       when :unique = true " +
            "           then count (distinct h.ip) " +
            "       else count(h.ip) " +
            "   end as hits) " +
            "from Hit as h " +
            "where h.created between :start and :end " +
            "   and (:uris is null or h.uri in :uris) " +
            "group by " +
            "   h.app, " +
            "   h.uri " +
            "order by " +
            "   hits desc")
    List<Stats> findAllByCreatedBetweenAndUriInAndUnique(@Param("start") LocalDateTime start,
                                                         @Param("end") LocalDateTime end,
                                                         @Param("uris") Set<String> uris,
                                                         @Param("unique") boolean unique);
}
