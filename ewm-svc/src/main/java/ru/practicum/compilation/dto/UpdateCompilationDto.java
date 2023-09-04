package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.Collection;

@AllArgsConstructor
@Getter
public class UpdateCompilationDto {
    private final Collection<Long> events;
    private final Boolean pinned;
    @Length(min = 1, max = 50)
    private final String tittle;
}
