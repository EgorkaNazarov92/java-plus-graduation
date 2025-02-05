package ru.practicum.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.request.RequestDto;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventRequestStatusUpdateResult {
	private List<RequestDto> confirmedRequests;
	private List<RequestDto> rejectedRequests;
}
