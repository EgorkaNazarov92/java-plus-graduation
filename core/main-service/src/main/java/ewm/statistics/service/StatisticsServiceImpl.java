package ewm.statistics.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import ewm.dto.EndpointHitDTO;
import ewm.dto.StatsRequestDTO;
import ewm.dto.ViewStatsDTO;
import ewm.StatsClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatisticsServiceImpl implements StatisticsService {
	private final StatsClient statsClient;

	@Override
	public void saveStats(HttpServletRequest request) {
		statsClient.createHit(EndpointHitDTO.builder()
				.app("ewm-main-service")
				.ip(request.getRemoteAddr())
				.uri(request.getRequestURI())
				.timestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.build());
	}

	@Override
	public Map<Long, Long> getStats(LocalDateTime start, LocalDateTime end, List<String> uris) {

		ObjectMapper mapper = new ObjectMapper();
		List<ViewStatsDTO> stats;

		StatsRequestDTO requestDTO = new StatsRequestDTO(
				start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				end.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
				uris,
				true);

		stats = statsClient.getHits(requestDTO);
		if (stats.isEmpty()) {
			return uris.stream().map(this::getEventIdFromUri)
					.collect(Collectors.toMap(Function.identity(), s -> 0L));
		} else {
			log.info("Данные статистики --> {}", stats);
			return stats.stream()
					.collect(Collectors.toMap(ViewStats -> getEventIdFromUri(ViewStats.getUri()),
							ViewStatsDTO::getHits));
		}
	}

	private Long getEventIdFromUri(String uri) {
		String[] parts = uri.split("/");
		return Long.parseLong(parts[parts.length - 1]);
	}
}
