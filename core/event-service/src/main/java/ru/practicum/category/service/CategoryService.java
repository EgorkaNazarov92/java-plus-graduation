package ru.practicum.category.service;


import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CreateCategoryDto;

import java.util.List;

public interface CategoryService {
	List<CategoryDto> getAll(Integer from, Integer size);

	CategoryDto getById(Long id);

	CategoryDto add(CreateCategoryDto createCategoryDto);

	CategoryDto update(Long id, CreateCategoryDto createCategoryDto);

	void delete(Long id);
}
