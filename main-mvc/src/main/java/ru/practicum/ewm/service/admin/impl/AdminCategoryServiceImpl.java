package ru.practicum.ewm.service.admin.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.category.CategoryDTO;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.mapper.category.CategoryMapper;
import ru.practicum.ewm.model.category.Category;
import ru.practicum.ewm.service.admin.AdminCategoryService;
import ru.practicum.ewm.storage.category.CategoryRepository;

@Service
@RequiredArgsConstructor
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository repository;

    @Override
    @Transactional
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category = CategoryMapper.fromDTO(categoryDTO);
        return CategoryMapper.toDTO(repository.save(category));
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        // TODO добавить проверку  наличие данной категории в таблице events
        checkCategory(catId);
        repository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long catId, CategoryDTO categoryDTO) {
        Category update = checkCategory(catId);
        //TODO возможно добавить проверку имени на одинаковость
        update.setName(categoryDTO.getName());
        return CategoryMapper.toDTO(repository.save(update));
    }

    private Category checkCategory(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " not found"));
    }


}
