package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.dto.compilation.CompilationDtoResponse;
import ru.practicum.dto.user.UserDto;

import java.util.List;

@Mapper
public interface CompilationMapper {
	CompilationMapper INSTANCE = Mappers.getMapper(CompilationMapper.class);

	CompilationDtoResponse compilationToCompilationDtoResponse(Compilation compilation);

	List<CompilationDtoResponse> mapListCompilations(List<Compilation> compilations);

	@Mapping(target = "id", source = "initiator")
	UserDto initiatorToDto(Long initiator);
}
