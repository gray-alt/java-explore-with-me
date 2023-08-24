package ru.practicum.dto;

import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.StatDto;
import ru.practicum.model.Hit;
import ru.practicum.model.Stat;

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

    public StatDto mapToStatDto(Stat stat) {
        return StatDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .hits(stat.getHits())
                .build();
    }

    public Collection<StatDto> mapToStatDto(Collection<Stat> stats) {
        return stats.stream().map(this::mapToStatDto).collect(Collectors.toList());
    }
}
