package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.requests.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.requests.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.utils.DateValidator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private List<User> users;
    private int counter;

    public UserService() {
        users = new ArrayList<>();
        counter = 1;
    }

    public UserDTO add(UserCreateRequest userCreateRequest) {
        User user = new User();

        if (!DateValidator.isValidBirthday(userCreateRequest.getBirthday())) {
            log.debug("User has invalid birthday date: " + userCreateRequest.getBirthday());
            return null;
        }

        user.setId(counter++);
        user.setUsername(userCreateRequest.getName());
        user.setBirthday(userCreateRequest.getBirthday());
        user.setLogin(userCreateRequest.getLogin());
        user.setEmail(userCreateRequest.getEmail());

        users.add(user);

        log.debug("User with id " + user.getId() + " created");
        return UserMapper.fromUserToDTO(user);
    }

    public User findById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public UserDTO update(UserUpdateRequest userUpdateRequest) {
        User user = findById(userUpdateRequest.getId());

        if (user == null) {
            log.debug("User with id " + userUpdateRequest.getId() + " not found");

            return null;
        }

        if (!DateValidator.isValidBirthday(userUpdateRequest.getBirthday())) {
            log.debug("User has invalid birthday date: " + userUpdateRequest.getBirthday());

            return null;
        }

        user.setUsername(userUpdateRequest.getName());
        user.setBirthday(userUpdateRequest.getBirthday());
        user.setLogin(userUpdateRequest.getLogin());
        user.setEmail(userUpdateRequest.getEmail());

        log.debug("User with id " + user.getId() + " updated");

        return UserMapper.fromUserToDTO(user);
    }

    public List<UserDTO> getAll() {
        return UserMapper.fromUsersToDTOs(users);
    }

}
