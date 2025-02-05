package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.event.EventDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CompilationDtoResponse {
	private Long id;
	private List<EventDto> events;
	private Boolean pinned;
	private String title;
}
