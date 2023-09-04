package ru.practicum.event.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
public class GetEventRequest {
    private Collection<Long> userIds;
    private Collection<EventState> states;
    private Collection<Long> categoryIds;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;

    private String text;
    private Boolean paid;
    private Boolean available;
    private EventSort sort;

    private int from;
    private int size;

    public static GetEventRequest of(Collection<Long> userIds, Collection<EventState> states,
                                     Collection<Long> categoryIds, LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                     String text, Boolean paid, Boolean available, EventSort sort,
                                     int from, int size) {
        GetEventRequest request = new GetEventRequest();
        request.setUserIds(userIds);
        request.setStates(states);
        request.setCategoryIds(categoryIds);
        request.setRangeStart(rangeStart);
        request.setRangeEnd(rangeEnd);
        request.setText(text);
        request.setPaid(paid);
        request.setAvailable(available);
        request.setSort(sort);
        request.setFrom(from);
        request.setSize(size);
        return request;
    }

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
}
