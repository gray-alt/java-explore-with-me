package ru.practicum.view.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Setter
//@Builder
//@Entity
//@Table(name = "events_views")
public class View {
    private Long event_id;
    private String ip;
}
