package ru.practicum.compilation.dto;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventMapper;

import java.util.Collection;
import java.util.stream.Collectors;

public class CompilationMapper {
    public static CompilationDto mapToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .events(EventMapper.mapToEventShortDto(compilation.getEvents()))
                .pinned(compilation.getPinned())
                .tittle(compilation.getTittle())
                .build();
    }

    public static Collection<CompilationDto> mapToCompilationDto(Collection<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::mapToCompilationDto).collect(Collectors.toList());
    }
}
