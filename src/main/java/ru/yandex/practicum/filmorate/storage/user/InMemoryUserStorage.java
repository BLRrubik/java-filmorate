package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.requests.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.requests.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.utils.DateValidator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private List<User> users;
    private int counter;

    public InMemoryUserStorage() {
        users = new ArrayList<>();
        counter = 1;
    }
    @Override
    public UserDTO add(UserCreateRequest userCreateRequest) {
        User user = new User();

        if (!DateValidator.isValidBirthday(userCreateRequest.getBirthday())) {
            log.debug("User has invalid birthday date: " + userCreateRequest.getBirthday());
            return null;
        }

        user.setId((long) counter++);
        user.setUsername(userCreateRequest.getName());
        user.setBirthday(userCreateRequest.getBirthday());
        user.setLogin(userCreateRequest.getLogin());
        user.setEmail(userCreateRequest.getEmail());

        users.add(user);

        log.debug("User with id " + user.getId() + " created");
        return UserMapper.fromUserToDTO(user);
    }

    @Override
    public User findById(Long id) {
        return users.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserDTO update(UserUpdateRequest userUpdateRequest) {
        User user = findById(userUpdateRequest.getId());

        if (user == null) {
            log.debug("User with id " + userUpdateRequest.getId() + " not found");
            throw new UserNotFoundException("User with id: " + userUpdateRequest.getId() + " not found");
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

    @Override
    public UserDTO getUser(Long id) {
        User user  = findById(id);

        if (user == null) {
            throw new UserNotFoundException("User with id: " + id + " not found");
        }

        return UserMapper.fromUserToDTO(user);
    }

    public List<UserDTO> getAll() {
        return UserMapper.fromUsersToDTOs(users);
    }
}
