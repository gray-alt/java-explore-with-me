package ru.practicum.compilation.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;

@RequiredArgsConstructor
@Getter
@Builder
public class CompilationDto {
    private final Collection<EventShortDto> events;
    private final Long id;
    private final Boolean pinned;
    private final String title;
}
