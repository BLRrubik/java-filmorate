package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO fromUserToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getUsername(),
                user.getBirthday()
        );
    }

    public static List<UserDTO> fromUsersToDTOs(List<User> users) {
        return users.stream()
                .map(UserMapper::fromUserToDTO)
                .collect(Collectors.toList());
    }
}
