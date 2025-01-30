package ru.practicum.controller;

import ru.dto.EndpointHitDTO;
import ru.dto.EndpointHitResponseDto;
import ru.dto.StatsRequestDTO;
import ru.dto.ViewStatsDTO;
import ru.practicum.service.HitService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class StatsController extends ErrorHandler {
    private HitService hitService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public EndpointHitResponseDto createHit(@RequestBody @Valid EndpointHitDTO endpointHitDto) {
        return hitService.create(endpointHitDto);
    }

    @GetMapping("/stats")
    public List<ViewStatsDTO> getHits(
            StatsRequestDTO statsRequestDTO
    ) {
        return hitService.getAll(statsRequestDTO);
    }
}
