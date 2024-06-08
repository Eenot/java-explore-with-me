package ru.practicum.ewm.main.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.main.dto.CategoryDto;
import ru.practicum.ewm.main.exception.ConflictDataException;
import ru.practicum.ewm.main.exception.IncorrectDataException;
import ru.practicum.ewm.main.exception.NoDataException;
import ru.practicum.ewm.main.mapper.CategoryMapper;
import ru.practicum.ewm.main.model.Category;
import ru.practicum.ewm.main.model.event.Event;
import ru.practicum.ewm.main.repository.CategoryRepository;
import ru.practicum.ewm.main.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        if (categoryDto.getName() == null || categoryDto.getName().isBlank()) {
            throw new IncorrectDataException("Поле: name. Ошибка: не должно быть пустым. Значение: null");
        }
        checkNameLength(categoryDto.getName().length());

        Category categoryWithName = categoryRepository.findCategoryByName(categoryDto.getName()).orElse(null);
        if (categoryWithName != null) {
            throw new ConflictDataException("Поле: name. Название должно быть уникальным!");
        }

        Category category = categoryRepository.save(CategoryMapper.toCategory(categoryDto));
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        if (categoryDto.getName() == null) {
            categoryDto.setName("");
        }

        checkNameLength(categoryDto.getName().length());
        Category categoryWithName = categoryRepository.findCategoryByName(categoryDto.getName()).orElse(null);
        if (categoryWithName != null && categoryWithName.getId() != categoryDto.getId()) {
            throw new ConflictDataException("Поле: name. Название должно быть уникальным!");
        }

        Category categoryFromDb = findCategory(categoryDto.getId());
        Category newCategory = CategoryMapper.toCategoryUpdate(categoryDto, categoryFromDb);
        categoryRepository.save(newCategory);
        return CategoryMapper.toCategoryDto(newCategory);
    }

    @Override
    public void deleteCategory(long categoryId) {
        findCategory(categoryId);
        List<Event> eventsWithCategory = eventRepository.findEventByCategory(categoryId);
        if (eventsWithCategory.size() > 0) {
            throw new ConflictDataException("Поле: category. Невозможно удалить категорию с событиями");
        }
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        Pageable page = PageRequest.of(from / size, size);
        return categoryRepository.getAllCategoriesWithPageable(page)
                .stream()
                .map(CategoryMapper::toCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryInfoById(long categoryId) {
        Category categoryFromDb = findCategory(categoryId);
        return CategoryMapper.toCategoryDto(categoryFromDb);
    }

    private void checkNameLength(long len) {
        if (len > 50) {
            throw new IncorrectDataException("Поле: name. Ошибка: длина должна быть < 50. Значение: " + len);
        }
    }

    private Category findCategory(long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NoDataException("Категория с id = " + id + " не найдена"));
    }
}
