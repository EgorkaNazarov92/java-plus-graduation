package ewm;

import ewm.dto.EndpointHitDTO;
import ewm.dto.EndpointHitResponseDto;
import ewm.dto.StatsRequestDTO;
import ewm.dto.ViewStatsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "stats-server")
public interface StatsClient {
	@PostMapping("/hit")
	EndpointHitResponseDto createHit(@RequestBody EndpointHitDTO endpointHitDto);

	@GetMapping("/stats")
	List<ViewStatsDTO> getHits(StatsRequestDTO statsRequestDTO);
}
