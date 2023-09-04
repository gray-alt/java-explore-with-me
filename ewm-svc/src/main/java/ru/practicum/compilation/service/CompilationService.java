package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;

import java.util.Collection;

public interface CompilationService {
    Compilation addCompilation(NewCompilationDto compilation);

    void deleteCompilation(Long compId);

    Compilation updateCompilation(Long compId, NewCompilationDto compilation);

    Collection<Compilation> getCompilations(Boolean pinned, int from, int size);

    Compilation getCompilationById(Long compId);
}
