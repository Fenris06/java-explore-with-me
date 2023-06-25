package ru.practicum.ewm.service.admin;

import ru.practicum.ewm.dto.category.CategoryDTO;

public interface AdminCategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    void deleteCategory(Long catId);

    CategoryDTO updateCategory(Long catId, CategoryDTO categoryDTO);
}
