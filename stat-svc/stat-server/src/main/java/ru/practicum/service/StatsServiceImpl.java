package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.model.Hit;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public void saveHit(Hit hit) {
        statsRepository.save(hit);
    }

    @Override
    public Collection<Stat> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean unique) {
        return statsRepository.findAllByCreatedBetweenAndUriInAndUnique(start, end, uris, unique);
    }
}
