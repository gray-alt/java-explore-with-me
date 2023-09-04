package ru.practicum.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.EventSort;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.GetEventRequest;
import ru.practicum.event.service.EventService;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping()
@Validated
public class PublicController {
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;

    // ПОДБОРКА СОБЫТИЙ
    @GetMapping("/compilations")
    public Collection<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        return CompilationMapper.mapToCompilationDto(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return null;
    }

    // КАТЕГОРИИ
    @GetMapping("/categories")
    public Collection<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        return CategoryMapper.mapToCategoryDto(categoryService.getAllCategory(from, size));
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        return CategoryMapper.mapToCategoryDto(categoryService.getCategoryById(catId));
    }

    // СОБЫТИЯ
    @GetMapping("/events")
    public Collection<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) Collection<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false) LocalDateTime rangeStart,
                                               @RequestParam(required = false) LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }

        GetEventRequest request = GetEventRequest.of(null, List.of(EventState.PUBLISHED), categories,
                rangeStart, rangeEnd, text, paid, onlyAvailable, sort, from, size);
        return EventMapper.mapToEventShortDto(eventService.getEventsByConditions(request));
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable Long id) {
        return EventMapper.mapToEventFullDto(eventService.getPublicEventById(id));
    }
}
