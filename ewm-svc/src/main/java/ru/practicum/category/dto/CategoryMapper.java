package ru.practicum.category.dto;

import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CategoryMapper {
    public static Category mapToCategory(CategoryDto categoryDto) {
        return new Category(categoryDto.getId(), categoryDto.getName());
    }

    public static Category mapToCategory(Long id, CategoryDto categoryDto) {
        return new Category(id, categoryDto.getName());
    }

    public static CategoryDto mapToCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }

    public static Collection<CategoryDto> mapToCategoryDto(Collection<Category> categories) {
        return categories.stream().map(CategoryMapper::mapToCategoryDto).collect(Collectors.toList());
    }
}
