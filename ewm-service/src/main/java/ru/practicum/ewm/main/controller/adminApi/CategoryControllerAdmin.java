package ru.practicum.ewm.main.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.service.category.CategoryService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/admin")
public class CategoryControllerAdmin {

    private final CategoryService categoryService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@RequestBody @Valid CategoryDto categoryDto) {
        log.debug("Admin: создание категории: {}", categoryDto);
        return categoryService.addCategory(categoryDto);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable long catId, @RequestBody CategoryDto categoryDto) {
        log.debug("Admin: обновление категории с id: {}", catId);
        categoryDto.setId(catId);
        return categoryService.updateCategory(categoryDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable long catId) {
        log.debug("Admin: удаление категории id: {}", catId);
        categoryService.deleteCategory(catId);
    }
}
