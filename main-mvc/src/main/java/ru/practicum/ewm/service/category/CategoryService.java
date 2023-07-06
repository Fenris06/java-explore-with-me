package ru.practicum.ewm.service.category;

import ru.practicum.ewm.dto.category.CategoryDTO;

import java.util.List;

public interface CategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    void deleteCategory(Long catId);

    CategoryDTO updateCategory(Long catId, CategoryDTO categoryDTO);

    List<CategoryDTO> getCategories(Integer from, Integer size);

    CategoryDTO getCategory(Long catId);
}
