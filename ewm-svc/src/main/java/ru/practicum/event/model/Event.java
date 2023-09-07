package ru.practicum.event.model;

import lombok.*;
import ru.practicum.category.model.Category;
import ru.practicum.location.model.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "create_date")
    private LocalDateTime createdOn;
    private String description;
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @ManyToOne
    @JoinColumn(name = "initiator_id")
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @Column(name = "is_paid")
    private Boolean paid;
    @Column(name = "participant_limit")
    private Long participantLimit;
    @Column(name = "published_date")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(value = EnumType.STRING)
    private EventState state;
    @Transient
    private EventStateAction stateAction;
    private String title;
    private int views;
    @ElementCollection
    @CollectionTable(name = "requests", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "status")
    private Set<String> eventRequests;
    @ElementCollection
    @CollectionTable(name = "events_views", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "ip")
    private Set<String> ipViews;

    public long getConfirmedRequestsCount() {
        if (eventRequests == null || eventRequests.isEmpty()) {
            return 0;
        }
        return eventRequests.stream().filter(s -> s.equals("CONFIRMED")).count();
    }

    public int getEventViews() {
        if (ipViews == null || ipViews.isEmpty()) {
            return  0;
        }
        return ipViews.size();
    }
}
