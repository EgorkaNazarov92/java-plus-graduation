package ru.practicum.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "user_actions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long userId;
	private Long eventId;
	private Double maxWeight;
	private Instant timestamp;
}
