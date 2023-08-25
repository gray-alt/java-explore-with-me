package ru.practicum.service;

import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

public interface StatsService {
    void saveHit(Hit hit);

    Collection<Stats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique);
}
