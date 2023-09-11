package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.InputCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.model.Compilation;

import java.util.Collection;

public interface CompilationService {
    Compilation addCompilation(InputCompilationDto compilation);

    void deleteCompilation(Long compId);

    Compilation updateCompilation(Long compId, UpdateCompilationDto compilation);

    Collection<Compilation> getCompilations(Boolean pinned, int from, int size);

    Compilation getCompilationById(Long compId);
}
