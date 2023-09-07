package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@AllArgsConstructor
@RestController
@RequestMapping("/admin")
@Slf4j
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
        log.info("Admin add category: " + categoryDto.toString());
        return CategoryMapper.mapToCategoryDto(
                categoryService.addCategory(CategoryMapper.mapToCategory(categoryDto))
        );
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Admin delete category with id=" + catId);
        categoryService.deleteCategoryById(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto renameCategory(@PathVariable Long catId, @RequestBody @Valid CategoryDto categoryDto) {
        log.info("Admin rename category with id=" + catId + ", " + categoryDto.toString());
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
        GetEventRequest request = GetEventRequest.of(users, states, categories, rangeStart, rangeEnd,
                null, null, null, null, from, size, null);
        log.info("Admin get events: " + request);
        return EventMapper.mapToEventFullDto(eventService.getEventsByConditions(request));
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId, @Valid @RequestBody UpdateEventDto updateEventDto) {
        log.info("Admin update event with id=" + eventId + ", " + updateEventDto.toString());
        return EventMapper.mapToEventFullDto(
                eventService.updateEventByAdmin(eventId, EventMapper.mapToEvent(updateEventDto)));
    }

    // ПОЛЬЗОВАТЕЛИ
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto addUser(@RequestBody @Valid UserDto userDto) {
        log.info("Admin create user: " + userDto.toString());
        return UserMapper.mapToUserDto(
                userService.addUser(UserMapper.mapToUser(userDto)));
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.info("Admin delete user with id=" + userId);
        userService.deleteUserById(userId);
    }

    @GetMapping("/users")
    public Collection<UserDto> getUsers(@RequestParam(required = false) Collection<Long> ids,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size) {
        log.info("Admin get users with ids=" + (ids == null ? "[]" : ids.toString()));
        return UserMapper.mapToUserDto(userService.getUsersByIds(ids, from, size));
    }

    // ПОДБОРКИ СОБЫТИЙ
    @PostMapping("/compilations")
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid InputCompilationDto inputCompilationDto) {
        log.info("Admin add compilation: " + inputCompilationDto.toString());
        return CompilationMapper.mapToCompilationDto(compilationService.addCompilation(inputCompilationDto));
    }

    @DeleteMapping("/compilations/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Admin delete compilation with id=" + compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/compilations/{compId}")
    public CompilationDto updateCompilation(@PathVariable Long compId,
                                            @RequestBody @Valid UpdateCompilationDto updateCompilationDto) {
        log.info("Admin update compilation: " + updateCompilationDto.toString());
        return CompilationMapper.mapToCompilationDto(compilationService.updateCompilation(compId, updateCompilationDto));
    }
}
