package ru.practicum.category.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.service.CategoryService;
import ru.practicum.dto.category.CategoryDto;
import ru.practicum.dto.category.CreateCategoryDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryAdminController {
	private final CategoryService categoryService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	CategoryDto addCategory(@RequestBody @Valid CreateCategoryDto createCategoryDto) {
		return categoryService.add(createCategoryDto);
	}

	@DeleteMapping("/{categoryId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	void deleteCategory(@PathVariable Long categoryId) {
		categoryService.delete(categoryId);
	}

	@PatchMapping("/{categoryId}")
	CategoryDto updateCategory(@PathVariable Long categoryId, @RequestBody @Valid CreateCategoryDto createCategoryDto) {
		return categoryService.update(categoryId, createCategoryDto);
	}
}
