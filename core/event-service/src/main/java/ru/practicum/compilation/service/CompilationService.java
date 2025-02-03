package ru.practicum.compilation.service;


import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationDtoResponse;
import ru.practicum.dto.compilation.CompilationDtoUpdate;

import java.util.List;

public interface CompilationService {
	CompilationDtoResponse createCompilation(CompilationDto compilationDto);

	void deleteCompilation(Long compId);

	CompilationDtoResponse updateCompilation(Long compId, CompilationDtoUpdate compilationDto);

	List<CompilationDtoResponse> getCompilations(Boolean pinned, Integer from, Integer size);

	CompilationDtoResponse getCompilation(Long compId);
}
