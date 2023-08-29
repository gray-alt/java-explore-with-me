package ru.practicum;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class StatsDto {
    private final String app;
    private final String uri;
    private final Long hits;
}
