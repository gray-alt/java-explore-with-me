package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.InputCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public Compilation addCompilation(InputCompilationDto compilation) {
        Compilation newCompilation = new Compilation();
        newCompilation.setEvents(getEventsByIds(compilation.getEvents()));
        newCompilation.setTitle(compilation.getTitle());
        newCompilation.setPinned(compilation.getPinned() != null && compilation.getPinned());
        log.info("Added compilation {} with id={}", newCompilation.getTitle(), newCompilation.getId());
        return compilationRepository.save(newCompilation);
    }

    @Override
    public void deleteCompilation(Long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new NotFoundException("Compilation with id=" + compId + " was not found");
        }
        compilationRepository.deleteById(compId);
        log.info("Deleted compilation with id={}", compId);
    }

    @Override
    public Compilation updateCompilation(Long compId, UpdateCompilationDto compilation) {
        Compilation foundCompilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id=" + compId + " was not found"));

        Collection<Event> events = getEventsByIds(compilation.getEvents());
        if (!events.isEmpty()) {
            foundCompilation.setEvents(events);
        }
        if (compilation.getPinned() != null) {
            foundCompilation.setPinned(compilation.getPinned());
        }
        if (compilation.getTitle() != null) {
            foundCompilation.setTitle(compilation.getTitle());
        }
        log.info("Updated compilation with id={}", compId);
        return compilationRepository.save(foundCompilation);
    }

    private Collection<Event> getEventsByIds(Set<Long> exodusIds) {
        Collection<Event> events = new ArrayList<>();
        if (exodusIds != null) {
            events = eventRepository.findAllById(exodusIds);
            if (events.size() < exodusIds.size()) {
                Set<Long> eventIds = events.stream().map(Event::getId).collect(Collectors.toSet());
                exodusIds.retainAll(eventIds);
                throw new NotFoundException("Events with ids=" + exodusIds + " was not found");
            }
        }
        return events;
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
