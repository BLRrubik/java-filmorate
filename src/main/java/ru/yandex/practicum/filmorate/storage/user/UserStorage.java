package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.requests.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.requests.user.UserUpdateRequest;

import java.util.List;

public interface UserStorage {
    UserDTO add(UserCreateRequest userCreateRequest);

    UserDTO update(UserUpdateRequest userUpdateRequest);

    UserDTO getUser(Long id);

    List<UserDTO> getAll();

    User findById(Long id);

}
