package ru.practicum.ewm.main.mapper;

import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.model.Category;

public class CategoryMapper {

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }

    public static Category toCategoryUpdate(CategoryDto newCategory, Category oldCategory) {
        if (newCategory.getName().isEmpty()) {
            newCategory.setName(null);
        }

        return Category.builder()
                .id(newCategory.getId())
                .name(newCategory.getName() != null ? newCategory.getName() : oldCategory.getName())
                .build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
