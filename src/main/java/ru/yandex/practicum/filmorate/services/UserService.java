package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public UserDTO addFriend(Long id, Long friendId) {
        User user = userStorage.findById(id);

        if (user == null) {
            throw new UserNotFoundException("User with id: " + id + " not found");
        }

        User friend = userStorage.findById(friendId);

        if (friend == null) {
            throw new UserNotFoundException("Friend with id: " + id + " not found");
        }

        user.addFriend(friend.getId());
        friend.addFriend(user.getId());

        return UserMapper.fromUserToDTO(user);
    }

    public UserDTO deleteFriend(Long id, Long friendId) {
        User user = userStorage.findById(id);

        if (user == null) {
            throw new UserNotFoundException("User with id: " + id + " not found");
        }

        User friend = userStorage.findById(friendId);

        if (friend == null) {
            throw new UserNotFoundException("Friend with id: " + id + " not found");
        }

        user.deleteFriend(friendId);
        friend.deleteFriend(id);

        return UserMapper.fromUserToDTO(user);
    }

    public List<UserDTO> getFriends(Long id) {
        User user = userStorage.findById(id);

        if (user == null) {
            throw new UserNotFoundException("User with id: " + id + " not found");
        }

        List<User> friends = user.getFriends().stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());

        return UserMapper.fromUsersToDTOs(friends);
    }

    public List<UserDTO> getCommonFriends(Long id, Long idToCheckCommon) {
        User user = userStorage.findById(id);

        if (user == null) {
            throw new UserNotFoundException("User with id: " + id + " not found");
        }

        User userToCheckCommon = userStorage.findById(idToCheckCommon);

        if (userToCheckCommon == null) {
            throw new UserNotFoundException("User to check common friends with id: " + id + " not found");
        }


        List<User> common = user.getFriends().stream()
                .filter(userToCheckCommon.getFriends()::contains)
                .map(userStorage::findById)
                .collect(Collectors.toList());

        return UserMapper.fromUsersToDTOs(common);
    }
}
