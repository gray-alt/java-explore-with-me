package ru.practicum.category.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class CategoryDto {
    private final Long id;
    @NotBlank
    @Length(min = 1, max = 50)
    private final String name;

    @Override
    public String toString() {
        return "CategoryDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
