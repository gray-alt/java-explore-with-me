package ru.practicum.location.model;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double lat;
    private Double lon;
    @Length(min = 3, max = 255)
    private String name;
    private Double radius;
    @Transient
    private boolean myLocation;
}
