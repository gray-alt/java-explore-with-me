package ru.practicum.service;

import ru.practicum.model.Hit;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsService {
    void saveHit(Hit hit);
    Collection<Stat> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean unique);
}
