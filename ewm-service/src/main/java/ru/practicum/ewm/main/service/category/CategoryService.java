package ru.practicum.ewm.main.service.category;

import ru.practicum.ewm.main.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto addCategory(CategoryDto categoryDto);

    CategoryDto updateCategory(CategoryDto categoryDto);

    void deleteCategory(long categoryId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategoryInfoById(long categoryId);
}
