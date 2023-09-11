package ru.practicum.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@Builder
public class UserShortDto {
    private final Long id;
    private final String name;
}
