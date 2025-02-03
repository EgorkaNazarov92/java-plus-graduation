package ru.practicum.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.practicum.category.model.Category;
import ru.practicum.dto.category.CategoryDto;

@Mapper
public interface CategoryMapper {
	CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

	CategoryDto categoryToCategoryDto(Category category);
}
