package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.requests.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {

    private final UserStorage userStorage;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(@Qualifier("db_user_storage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserDTO addFriend(Long id, Long friendId) {
        User user = userStorage.findById(id);

        User friend = userStorage.findById(friendId);

        user.addFriend(friend.getId());
        friend.addFriend(user.getId());

        String sql = "insert into friends (user_id, friend_id, status_id) values (?,?,1);";

        jdbcTemplate.update(sql, user.getId(), friend.getId());
        jdbcTemplate.update(sql, friend.getId(), user.getId());



        return UserMapper.fromUserToDTO(user);
    }

    public UserDTO deleteFriend(Long id, Long friendId) {
        User user = userStorage.findById(id);
        User friend = userStorage.findById(friendId);

        user.deleteFriend(friendId);
        friend.deleteFriend(id);

        String sql = "delete from friends where user_id = ? and friend_id = ?";

        jdbcTemplate.update(sql, user.getId(), friend.getId());
        jdbcTemplate.update(sql, friend.getId(), user.getId());

        return UserMapper.fromUserToDTO(user);
    }

    public List<UserDTO> getFriends(Long id) {
        User user = userStorage.findById(id);

        List<User> friends = user.getFriends().stream()
                .map(userStorage::findById)
                .collect(Collectors.toList());

        return UserMapper.fromUsersToDTOs(friends);
    }

    public List<UserDTO> getCommonFriends(Long id, Long idToCheckCommon) {
        User user = userStorage.findById(id);

        User userToCheckCommon = userStorage.findById(idToCheckCommon);

        List<User> common = user.getFriends().stream()
                .filter(userToCheckCommon.getFriends()::contains)
                .map(userStorage::findById)
                .collect(Collectors.toList());

        return UserMapper.fromUsersToDTOs(common);
    }
}
