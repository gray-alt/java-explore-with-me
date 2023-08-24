package ru.practicum;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
@Getter
public class StatDto {
    private final String app;
    private final String uri;
    private final Long hits;
}
