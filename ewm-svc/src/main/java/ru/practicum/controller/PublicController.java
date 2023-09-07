package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.StatsClient;
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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping()
@Validated
@Slf4j
public class PublicController {
    private final CategoryService categoryService;
    private final EventService eventService;
    private final CompilationService compilationService;
    private final StatsClient statsClient;

    // ПОДБОРКА СОБЫТИЙ
    @GetMapping("/compilations")
    public Collection<CompilationDto> getCompilations(@RequestParam(required = false) boolean pinned,
                                                      @RequestParam(defaultValue = "0") int from,
                                                      @RequestParam(defaultValue = "10") int size) {
        log.info("Public get compilations with pinned=" + pinned);
        return CompilationMapper.mapToCompilationDto(compilationService.getCompilations(pinned, from, size));
    }

    @GetMapping("/compilations/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("Public get compilations with comId=" + compId);
        return CompilationMapper.mapToCompilationDto(compilationService.getCompilationById(compId));
    }

    // КАТЕГОРИИ
    @GetMapping("/categories")
    public Collection<CategoryDto> getCategories(@RequestParam(defaultValue = "0") int from,
                                                 @RequestParam(defaultValue = "10") int size) {
        log.info("Public get categories");
        return CategoryMapper.mapToCategoryDto(categoryService.getAllCategory(from, size));
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        log.info("Public get category with catId=" + catId);
        return CategoryMapper.mapToCategoryDto(categoryService.getCategoryById(catId));
    }

    // СОБЫТИЯ
    @GetMapping("/events")
    public Collection<EventShortDto> getEvents(@RequestParam(required = false) String text,
                                               @RequestParam(required = false) Collection<Long> categories,
                                               @RequestParam(required = false) Boolean paid,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                               @RequestParam(required = false)
                                               @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                               @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                               @RequestParam(defaultValue = "EVENT_DATE") EventSort sort,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size,
                                               HttpServletRequest httpRequest) {
        statsClient.createHit("ewm-svc", httpRequest.getRequestURI(), httpRequest.getRemoteAddr());

        if (rangeStart == null && rangeEnd == null) {
            rangeStart = LocalDateTime.now();
        }
        GetEventRequest request = GetEventRequest.of(null, List.of(EventState.PUBLISHED), categories,
                rangeStart, rangeEnd, text, paid, onlyAvailable, sort, from, size, httpRequest.getRemoteAddr());
        log.info("Public get event requests: " + request);
        return EventMapper.mapToEventShortDto(eventService.getEventsByConditions(request));
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEvent(@PathVariable Long id, HttpServletRequest httpRequest) {
        statsClient.createHit("ewm-svc", httpRequest.getRequestURI(), httpRequest.getRemoteAddr());
        log.info("Public get event with id=" + id);
        return EventMapper.mapToEventFullDto(eventService.getPublicEventById(id, httpRequest.getRemoteAddr()));
    }
}
