package ru.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.dto.user.UserDto;

@FeignClient(name = "user-service")
public interface UserClient {

	@GetMapping("/int/users/{userId}")
	UserDto getUser(@PathVariable Long userId);
}
