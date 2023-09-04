package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.Collection;

@AllArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(Long catId) {
        Category foundCategory = findAndCheckCategory(catId);
        categoryRepository.delete(foundCategory);
    }

    @Override
    public Category renameCategory(Category category) {
        findAndCheckCategory(category.getId());
        return categoryRepository.save(category);
    }

    @Override
    public Category getCategoryById(Long catId) {
        Category foundCategory = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " was not found"));
        return foundCategory;
    }

    private Category findAndCheckCategory(Long catId) {
        Category foundCategory = categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " was not found"));
        if (eventRepository.existsByCategoryId(catId)) {
            throw new ConflictException("The category is not empty");
        }
        return foundCategory;
    }

    @Override
    public Collection<Category> getAllCategory(int from, int size) {
        int page = from > 0 ? from / size : 0;
        PageRequest pageRequest = PageRequest.of(page, size);
        return categoryRepository.findAll(pageRequest).toList();
    }
}
