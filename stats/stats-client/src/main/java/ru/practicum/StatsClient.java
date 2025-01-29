package ru.practicum;

import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;
import ru.dto.EndpointHitDTO;
import ru.dto.EndpointHitResponseDto;
import ru.dto.StatsRequestDTO;
import ru.dto.ViewStatsDTO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(name = "stats-server")
public interface StatsClient {
	@PostMapping("/hit")
	EndpointHitResponseDto createHit(@RequestBody EndpointHitDTO endpointHitDto);

	@RequestMapping(method = RequestMethod.GET, value = "/stats")
	List<ViewStatsDTO> getHits(@SpringQueryMap StatsRequestDTO statsRequestDTO);
}
