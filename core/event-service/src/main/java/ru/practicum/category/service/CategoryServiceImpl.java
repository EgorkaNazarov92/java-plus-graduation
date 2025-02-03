package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CreateCategoryDto;
import ru.practicum.error.exception.ConflictException;
import ru.practicum.error.exception.ExistException;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.EventRepository;
import ru.practicum.event.model.Event;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
	private final CategoryRepository categoryRepository;
	private final EventRepository eventRepository;
	private static final String CATEGORY_NOT_FOUND = "Category not found";
	private static final String CATEGORY_NAME_EXIST = "Category with this name already exist";

	@Override
	public List<CategoryDto> getAll(Integer from, Integer size) {
		Pageable pageable = PageRequest.of(from, size);
		return categoryRepository.findAll(pageable).stream().map(CategoryMapper.INSTANCE::categoryToCategoryDto).toList();
	}

	@Override
	public CategoryDto getById(Long id) {
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isEmpty()) {
			throw new NotFoundException(CATEGORY_NOT_FOUND);
		}
		return CategoryMapper.INSTANCE.categoryToCategoryDto(category.get());
	}

	@Override
	public CategoryDto add(CreateCategoryDto createCategoryDto) {
		try {
			Category category = categoryRepository.save(Category.builder().name(createCategoryDto.getName()).build());
			return CategoryMapper.INSTANCE.categoryToCategoryDto(category);
		} catch (DataAccessException e) {
			throw new ExistException(CATEGORY_NAME_EXIST);
		}
	}

	@Override
	public CategoryDto update(Long id, CreateCategoryDto createCategoryDto) {
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isEmpty()) {
			throw new NotFoundException(CATEGORY_NOT_FOUND);
		}
		Category categoryToUpdate = category.get();
		categoryToUpdate.setName(createCategoryDto.getName());
		try {
			Category updated = categoryRepository.save(categoryToUpdate);
			return CategoryMapper.INSTANCE.categoryToCategoryDto(updated);
		} catch (DataAccessException e) {
			throw new ExistException(CATEGORY_NAME_EXIST);
		}
	}

	@Override
	public void delete(Long id) {
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isEmpty()) {
			throw new NotFoundException(CATEGORY_NOT_FOUND);
		}
		List<Event> events = eventRepository.findByCategoryId(id);
		if (!events.isEmpty()) throw new ConflictException("Есть привязанные события.");
		categoryRepository.deleteById(id);
	}
}
