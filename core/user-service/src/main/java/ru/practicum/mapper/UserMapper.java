package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.user.UserDto;
import ru.practicum.model.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {
	public static User mapToUser(UserDto userDto) {
		return User.builder()
				.id(userDto.getId())
				.name(userDto.getName())
				.email(userDto.getEmail())
				.build();
	}

	public static UserDto mapToUserDto(User user) {
		return new UserDto(
				user.getId(),
				user.getName(),
				user.getEmail()
		);
	}

	public static List<UserDto> mapToUserDto(Iterable<User> users) {
		List<UserDto> dtos = new ArrayList<>();
		for (User user : users) {
			dtos.add(mapToUserDto(user));
		}
		return dtos;
	}
}
