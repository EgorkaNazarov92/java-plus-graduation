package ru.practicum.controller.internal;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.user.UserDto;
import ru.practicum.service.UserService;

@RestController
@Validated
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/int/users")
public class UserInternalController {
	private final UserService userService;

	@GetMapping("/{userId}")
	public UserDto getUser(@PathVariable Long userId) {
		log.info("Получить пользователя по userId --> {}", userId);
		return userService.getUser(userId);
	}
}
