package ru.practicum.category.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;

import java.util.Collection;

@AllArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public Category addCategory(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new ConflictException("Category with name " + category.getName() + " is already exist");
        }
        category = categoryRepository.save(category);
        log.info("Added category " + category.getName() + " with id=" + category.getId());
        return category;
    }

    @Override
    public void deleteCategoryById(Long catId) {
        Category foundCategory = findAndCheckCategory(catId);
        categoryRepository.delete(foundCategory);
        log.info("Deleted category " + foundCategory.getName() + " with id=" + foundCategory.getId());
    }

    @Override
    public Category renameCategory(Category category) {
        if (categoryRepository.existsByNameAndIdNot(category.getName(), category.getId())) {
            throw new ConflictException("Category with name " + category.getName() + " is already exist");
        }
        Category foundCategory = findAndCheckCategory(category.getId());
        foundCategory.setName(category.getName());
        log.info("Rename category " + foundCategory.getName() + " with id=" + foundCategory.getId());
        return categoryRepository.save(foundCategory);
    }

    @Override
    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(
                () -> new NotFoundException("Category with id=" + catId + " was not found"));
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
