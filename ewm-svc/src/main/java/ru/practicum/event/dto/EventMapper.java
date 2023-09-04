package ru.practicum.event.dto;

import ru.practicum.category.dto.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.location.dto.LocationMapper;
import ru.practicum.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

public class EventMapper {
    public static Event mapToEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(Category.builder().id(newEventDto.getCategory()).build())
                .createdOn(LocalDateTime.now())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .location(LocationMapper.mapToLocation(newEventDto.getLocation()))
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.isRequestModeration())
                .state(EventState.SEND_TO_REVIEW)
                .title(newEventDto.getTitle())
                .build();
    }

    public static Event mapToEvent(UpdateEventDto updateEventDto) {
        return Event.builder()
                .annotation(updateEventDto.getAnnotation())
                .category(Category.builder().id(updateEventDto.getCategory()).build())
                .description(updateEventDto.getDescription())
                .eventDate(updateEventDto.getEventDate())
                .location(LocationMapper.mapToLocation(updateEventDto.getLocation()))
                .paid(updateEventDto.getPaid())
                .participantLimit(updateEventDto.getParticipantLimit())
                .requestModeration(updateEventDto.getRequestModeration())
                .state(updateEventDto.getStateAction())
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
                .title(event.getTitle())
                .views(event.getViews())
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
                .views(event.getViews())
                .build();
    }

    public static Collection<EventShortDto> mapToEventShortDto(Collection<Event> events) {
        return events.stream().map(EventMapper::mapToEventShortDto).collect(Collectors.toList());
    }

    public static Collection<EventFullDto> mapToEventFullDto(Collection<Event> events) {
        return events.stream().map(EventMapper::mapToEventFullDto).collect(Collectors.toList());
    }
}