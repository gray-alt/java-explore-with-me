package ru.practicum.category.service;

import ru.practicum.category.model.Category;

import java.util.Collection;

public interface CategoryService {
    Category addCategory(Category category);

    void deleteCategoryById(Long catId);

    Category renameCategory(Category category);

    Category getCategoryById(Long catId);

    Collection<Category> getAllCategory(int from, int size);
}
