package ru.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "event_similarity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventSimilarity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long eventA;
	private Long eventB;
	private Float score;
	private Instant timestamp;
}
