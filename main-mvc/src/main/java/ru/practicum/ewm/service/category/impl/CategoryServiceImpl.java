package ru.practicum.ewm.service.category.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.category.CategoryDTO;
import ru.practicum.ewm.exception.DataValidationException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.category.CategoryMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.service.category.CategoryService;
import ru.practicum.ewm.storage.category.CategoryRepository;
import ru.practicum.ewm.storage.event.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.fromDTO(categoryDTO);
        return CategoryMapper.toDTO(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        checkCategory(catId);
        checkUseCategory(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long catId, CategoryDTO categoryDTO) {
        Category update = checkCategory(catId);
        update.setName(categoryDTO.getName());
        return CategoryMapper.toDTO(categoryRepository.save(update));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategories(Integer from, Integer size) {
        PageRequest page = PageRequest.of(from / size, size);
        return categoryRepository.findAll(page)
                .stream()
                .map(CategoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategory(Long catId) {
        return CategoryMapper.toDTO(checkCategory(catId));
    }

    private Category checkCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " not found"));
    }

    private void checkUseCategory(Long catId) {
        PageRequest page = PageRequest.of(0, 1);
        List<Event> events = eventRepository.findByCategory_Id(catId, page);
        if (!events.isEmpty()) {
            throw new DataValidationException("The category is not empty");
        }
    }
}
