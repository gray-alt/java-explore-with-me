package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@Getter
public class NewCompilationDto {
    @NotNull
    private final Set<Long> events;
    @NotNull
    private final Boolean pinned;
    @NotBlank
    @Length(min = 1, max = 50)
    private final String tittle;
}
