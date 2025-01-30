package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationDtoResponse;
import ru.practicum.compilation.dto.CompilationDtoUpdate;

import java.util.List;

public interface CompilationService {
	CompilationDtoResponse createCompilation(CompilationDto compilationDto);

	void deleteCompilation(Long compId);

	CompilationDtoResponse updateCompilation(Long compId, CompilationDtoUpdate compilationDto);

	List<CompilationDtoResponse> getCompilations(Boolean pinned, Integer from, Integer size);

	CompilationDtoResponse getCompilation(Long compId);
}
