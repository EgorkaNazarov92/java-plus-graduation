package ru.practicum.mapper;

import ru.dto.EndpointHitResponseDto;
import ru.practicum.model.Hit;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HitMapper {
    HitMapper INSTANCE = Mappers.getMapper(HitMapper.class);

    EndpointHitResponseDto hitToEndpointHitResponseDto(Hit hit);
}
