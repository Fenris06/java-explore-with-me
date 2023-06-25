package ru.practicum.ewm.mapper.category;

import ru.practicum.ewm.dto.category.CategoryDTO;
import ru.practicum.ewm.model.category.Category;

public class CategoryMapper {

    public static Category fromDTO(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return category;
    }

    public static CategoryDTO toDTO(Category category) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
