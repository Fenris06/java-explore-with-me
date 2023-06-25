package ru.practicum.ewm.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryDTO;
import ru.practicum.ewm.service.admin.AdminCategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
@Validated
public class AdminCategoryController {
    private final AdminCategoryService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO createCategory(@RequestBody @Valid CategoryDTO categoryDTO) {
        return service.createCategory(categoryDTO);
    }

    @DeleteMapping("{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable("catId") Long catId) {
        service.deleteCategory(catId);
    }

    @PatchMapping("{catId}")
    public CategoryDTO updateCategory(@PathVariable("catId") Long catId, @RequestBody @Valid CategoryDTO categoryDTO) {
        return service.updateCategory(catId, categoryDTO);
    }


}
