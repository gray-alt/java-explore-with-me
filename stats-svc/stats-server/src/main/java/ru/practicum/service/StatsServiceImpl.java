package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void saveHit(Hit hit) {
        statsRepository.save(hit);
    }

    @Override
    public Collection<Stats> getStats(LocalDateTime start, LocalDateTime end, Set<String> uris, boolean unique) {
        return statsRepository.findAllByCreatedBetweenAndUriInAndUnique(start, end, uris, unique);
    }
}
