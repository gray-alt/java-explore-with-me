package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.service.CategoryService;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.InputCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.UpdateEventDto;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.GetEventRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserMapper;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collection;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin")
@Validated
public class AdminController {
    private final CategoryService categoryService;
    private final UserService userService;
    private final EventService eventService;
    private final CompilationService compilationService;

    // КАТЕГОРИИ
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto addCategory(@RequestBody @Valid CategoryDto categoryDto) {
        return CategoryMapper.mapToCategoryDto(
                categoryService.addCategory(CategoryMapper.mapToCategory(categoryDto))
        );
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        categoryService.deleteCategoryById(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto renameCategory(@PathVariable Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        return CategoryMapper.mapToCategoryDto(
                categoryService.renameCategory(CategoryMapper.mapToCategory(catId, categoryDto))
        );
    }

    // СОБЫТИЯ
    @GetMapping("/events")
    public Collection<EventFullDto> getEvents(@RequestParam(required = false) Collection<Long> users,
                                              @RequestParam(required = false) Collection<EventState> states,
                                              @RequestParam(required = false) Collection<Long> categories,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                              @RequestParam(required = false)
                                              @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) {
        GetEventRequest request =GetEventRequest.builder()
                .userIds(users)
                .states(states)
                .categoryIds(categories)
                .rangeStart(rangeStart)
                .rangeEnd(rangeEnd)
                .from(from)
                .size(size)
                .build();

        return EventMapper.mapToEventFullDto(eventService.getEventsByConditions(request));
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @Valid @RequestBody UpdateEventDto updateEventDto) {
        return EventMapper.mapToEventFullDto(
                eventService.updateEventByAdmin(eventId, EventMapper.mapToEvent(updateEventDto)));
    }

    // ПОЛЬЗОВАТЕЛИ
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        return UserMapper.mapToUserDto(
                userService.addUser(UserMapper.mapToUser(userDto)));
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUserById(userId);
    }

    @GetMapping("/users")
    public Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        return UserMapper.mapToUserDto(userService.getUsersByIds(ids, from, size));
    }

    // ПОДБОРКИ СОБЫТИЙ
    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid InputCompilationDto inputCompilationDto) {
        return CompilationMapper.mapToCompilationDto(compilationService.addCompilation(inputCompilationDto));
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid UpdateCompilationDto updateCompilationDto) {
        return CompilationMapper.mapToCompilationDto(compilationService.updateCompilation(compId, updateCompilationDto));
    }
}
