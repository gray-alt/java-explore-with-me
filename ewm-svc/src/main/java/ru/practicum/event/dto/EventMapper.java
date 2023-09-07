package ru.practicum.event.dto;

import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.location.dto.LocationMapper;
import ru.practicum.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventMapper {
    public static Event mapToEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(CategoryMapper.mapToCategory(newEventDto.getCategory()))
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(LocationMapper.mapToLocation(newEventDto.getLocation()))
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration())
                .state(EventState.PENDING)
                .title(newEventDto.getTitle())
                .build();
    }

    public static Event mapToEvent(UpdateEventDto updateEventDto) {
        return Event.builder()
                .annotation(updateEventDto.getAnnotation())
                .category(CategoryMapper.mapToCategory(updateEventDto.getCategory()))
                .description(updateEventDto.getDescription())
                .eventDate(updateEventDto.getEventDate())
                .location(LocationMapper.mapToLocation(updateEventDto.getLocation()))
                .paid(updateEventDto.getPaid())
                .participantLimit(updateEventDto.getParticipantLimit())
                .requestModeration(updateEventDto.getRequestModeration())
                .stateAction(updateEventDto.getStateAction())
                .title(updateEventDto.getTitle())
                .build();
    }

    public static EventFullDto mapToEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequestsCount())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .location(LocationMapper.mapToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getEventViews())
                .build();
    }

    public static EventShortDto mapToEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.mapToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequestsCount())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.mapToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getEventViews())
                .build();
    }

    public static Collection<EventShortDto> mapToEventShortDto(Collection<Event> events) {
        return events.stream().map(EventMapper::mapToEventShortDto).collect(Collectors.toList());
    }

    public static Collection<EventFullDto> mapToEventFullDto(Collection<Event> events) {
        return events.stream().map(EventMapper::mapToEventFullDto).collect(Collectors.toList());
    }
}