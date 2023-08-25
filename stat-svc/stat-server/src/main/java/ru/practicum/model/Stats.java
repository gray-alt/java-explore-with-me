package ru.practicum.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class Stats {
    private String app;
    private String uri;
    private Long hits;
}
