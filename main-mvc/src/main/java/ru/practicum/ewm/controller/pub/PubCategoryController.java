package ru.practicum.ewm.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.category.CategoryDTO;
import ru.practicum.ewm.service.category.CategoryService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class PubCategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDTO> getCategories(@RequestParam(name = "from", required = false, defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(name = "size", required = false, defaultValue = "10") @Min(1) Integer size) {
        return categoryService.getCategories(from, size);
    }

    @GetMapping("{catId}")
    public CategoryDTO getCategory(@PathVariable("catId") @Min(1) Long catId) {
        return categoryService.getCategory(catId);
    }
}
