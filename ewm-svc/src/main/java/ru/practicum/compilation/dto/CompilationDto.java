package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.practicum.event.dto.EventShortDto;

import java.util.Collection;

@AllArgsConstructor
@Getter
@Builder
public class CompilationDto {
    private final Collection<EventShortDto> events;
    private final Long id;
    private final Boolean pinned;
    private final String title;
}
