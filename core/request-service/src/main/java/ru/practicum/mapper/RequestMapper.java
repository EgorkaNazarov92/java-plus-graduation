package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.model.Request;

import java.util.List;

@Mapper
public interface RequestMapper {
	RequestMapper INSTANCE = Mappers.getMapper(RequestMapper.class);

	@Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
	RequestDto mapToRequestDto(Request request);

	List<RequestDto> mapListRequests(List<Request> requests);

	List<Request> mapListDtoToRequests(List<RequestDto> requests);

	@Mapping(source = "created", target = "created", dateFormat = "yyyy-MM-dd HH:mm:ss")
	Request mapToRequest(RequestDto request);
}
