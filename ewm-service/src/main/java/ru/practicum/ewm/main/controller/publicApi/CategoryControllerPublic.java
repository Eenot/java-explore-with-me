package ru.practicum.ewm.main.controller.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.CategoryService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/categories")
public class CategoryControllerPublic {

    private final CategoryService categoryService;

    @GetMapping()
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0") @Min(0) int from,
                                           @RequestParam(defaultValue = "10") @Min(0) int size) {
        log.debug("Public: получение категорий");
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{categoryId}")
    public CategoryDto getCategoryById(@PathVariable @Min(0) long categoryId) {
        log.debug("Public: получение категории с id: {}", categoryId);
        return categoryService.getCategoryInfoById(categoryId);
    }
}
