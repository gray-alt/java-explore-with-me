package ru.practicum.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class Stat {
    private String app;
    private String uri;
    private Long hits;
}
