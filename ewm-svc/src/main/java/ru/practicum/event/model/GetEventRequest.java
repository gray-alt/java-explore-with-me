package ru.practicum.event.model;

import lombok.*;
import ru.practicum.location.model.Location;

import java.time.LocalDateTime;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GetEventRequest {
    private Collection<Long> userIds;
    private Collection<EventState> states;
    private Collection<Long> categoryIds;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Collection<Long> locationIds;
    private Location searchLocation;
    private String text;
    private Boolean paid;
    private Boolean available;
    private EventSort sort;
    private int from;
    private int size;
    private String ip;

    public boolean hasUserIds() {
        return userIds != null && !userIds.isEmpty();
    }

    public boolean hasStates() {
        return states != null && !states.isEmpty();
    }

    public boolean hasCategoryIds() {
        return categoryIds != null && !categoryIds.isEmpty();
    }

    public boolean hasRange() {
        return rangeStart != null || rangeEnd != null;
    }

    public boolean hasLocationIds() {
        return locationIds != null && !locationIds.isEmpty();
    }

    @Override
    public String toString() {
        return "GetEventRequest{" +
                "userIds=" + userIds +
                ", states=" + states +
                ", categoryIds=" + categoryIds +
                ", rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                ", text='" + text + '\'' +
                ", paid=" + paid +
                ", available=" + available +
                ", sort=" + sort +
                ", from=" + from +
                ", size=" + size +
                '}';
    }
}
