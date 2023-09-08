package ru.practicum.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Builder
@Getter
public class ErrorResponse {
    private final String status;
    private final String reason;
    private final String message;
    private final LocalDateTime timestamp;
}
