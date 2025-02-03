package ru.practicum.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.CategoryService;
import ru.practicum.dto.category.CategoryDto;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
	private final CategoryService categoryService;

	@GetMapping
	public List<CategoryDto> getCategories(
			@RequestParam(defaultValue = "0") Integer from,
			@RequestParam(defaultValue = "10") Integer size
	) {
		return categoryService.getAll(from, size);
	}

	@GetMapping("/{categoryId}")
	public CategoryDto getCategory(@PathVariable Long categoryId) {
		return categoryService.getById(categoryId);
	}
}
