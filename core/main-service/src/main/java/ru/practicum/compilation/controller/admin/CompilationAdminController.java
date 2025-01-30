package ru.practicum.compilation.controller.admin;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationDtoResponse;
import ru.practicum.compilation.dto.CompilationDtoUpdate;
import ru.practicum.compilation.service.CompilationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
	private final CompilationService compilationService;

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	public CompilationDtoResponse createCompilation(@RequestBody @Valid CompilationDto compilationDto) {
		log.info("Добавить подборку Compilation --> {}", compilationDto);
		return compilationService.createCompilation(compilationDto);
	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{compId}")
	public void deleteCompilation(@PathVariable Long compId) {
		log.info("Удалить подборку по compId --> {}", compId);
		compilationService.deleteCompilation(compId);
	}

	@PatchMapping("/{compId}")
	CompilationDtoResponse updateCompilation(@PathVariable Long compId,
											 @RequestBody @Valid CompilationDtoUpdate compilationDto) {
		log.info("Обновить подборку по compId --> {}, подборка --> {}", compId, compilationDto);
		return compilationService.updateCompilation(compId, compilationDto);
	}
}
