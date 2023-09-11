package ru.practicum.compilation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@RequiredArgsConstructor
@Getter
public class UpdateCompilationDto {
    private final Set<Long> events;
    private final Boolean pinned;
    @Length(min = 1, max = 50)
    private final String title;

    @Override
    public String toString() {
        return "InputCompilationDto{" +
                "events=" + events +
                ", pinned=" + pinned +
                ", title='" + title + '\'' +
                '}';
    }
}
