package ru.practicum.compilation.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public Compilation addCompilation(NewCompilationDto compilation) {
        Compilation newCompilation = getNewCompilation(compilation);
        return compilationRepository.save(newCompilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public Compilation updateCompilation(Long compId, NewCompilationDto compilation) {
        if (!compilationRepository.existsById(compId)) {
                throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
        Compilation newCompilation = getNewCompilation(compilation);
        newCompilation.setId(compId);
        return compilationRepository.save(newCompilation);
    }

    private Compilation getNewCompilation(NewCompilationDto compilation) {
        Set<Long> exodusIds = compilation.getEvents();
        Collection<Event> events = eventRepository.findAllById(exodusIds);
        if (events.size() < exodusIds.size()) {
            Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
            exodusIds.retainAll(eventIds);
            throw new NotFoundException("Events with id=" + exodusIds + " was not found");
        }

        Compilation newCompilation = new Compilation();
        newCompilation.setEvents(events);
        newCompilation.setPinned(newCompilation.getPinned());
        newCompilation.setTittle(newCompilation.getTittle());

        return newCompilation;
    }

    @Override
    public Collection<Compilation> getCompilations(Boolean pinned, int from, int size) {
        int page = from > 0 ? from / size : 0;
        PageRequest pageRequest = PageRequest.of(page, size);
        if (pinned == null) {
            return compilationRepository.findAll(pageRequest).toList();
        }
        return compilationRepository.findAllByPinned(pinned, pageRequest);
    }

    @Override
    public Compilation getCompilationById(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id=" + compId + " was not found"));
    }
}
