package ru.practicum.dto;

import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatsDto;
import ru.practicum.model.Hit;
import ru.practicum.model.Stats;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class StatsMapper {
    public Hit mapToHit(HitDto hitDto) {
        return Hit.builder()
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .created(hitDto.getTimestamp())
                .build();
    }

    public StatsDto mapToStatDto(Stats stats) {
        return StatsDto.builder()
                .app(stats.getApp())
                .uri(stats.getUri())
                .hits(stats.getHits())
                .build();
    }

    public Collection<StatsDto> mapToStatDto(Collection<Stats> stats) {
        return stats.stream().map(this::mapToStatDto).collect(Collectors.toList());
    }
}
