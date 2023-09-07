package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@AllArgsConstructor
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
