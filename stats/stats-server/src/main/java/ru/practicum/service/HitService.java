package ru.practicum.service;

import ru.dto.EndpointHitDTO;
import ru.dto.EndpointHitResponseDto;
import ru.dto.StatsRequestDTO;
import ru.dto.ViewStatsDTO;

import java.util.List;

public interface HitService {
    EndpointHitResponseDto create(EndpointHitDTO endpointHitDTO);

    List<ViewStatsDTO> getAll(StatsRequestDTO statsRequestDTO);
}
