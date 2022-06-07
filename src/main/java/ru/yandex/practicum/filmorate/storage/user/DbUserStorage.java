package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.mappers.UserMapper;
import ru.yandex.practicum.filmorate.requests.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.requests.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.utils.DateValidator;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Component("db_user_storage")
public class DbUserStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DbUserStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public UserDTO add(UserCreateRequest userCreateRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        if (!DateValidator.isValidBirthday(userCreateRequest.getBirthday())) {
            return null;
        }

        String sql = "insert into users (name, login, email, birthday) " +
                "values (?,?,?,?);";

        int status = jdbcTemplate.update(con -> {
                    PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
                    stmt.setString(1,
                            userCreateRequest.getName().isEmpty() ?
                                    userCreateRequest.getLogin() : userCreateRequest.getName());
                    stmt.setString(2,userCreateRequest.getLogin());
                    stmt.setString(3,userCreateRequest.getEmail());
                    stmt.setDate(4, Date.valueOf(userCreateRequest.getBirthday()));
                    return stmt;
                },
                keyHolder
        );

        if (status == 1) {
            return UserMapper.fromUserToDTO(findById(keyHolder.getKey().longValue()));
        }

        return null;
    }

    @Override
    public UserDTO update(UserUpdateRequest userUpdateRequest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "update users set name = ?, login = ?, email=?, birthday=? " +
                "where user_id = ?;";

        int status = jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(sql, new String[]{"user_id"});
            stmt.setString(1,
                    userUpdateRequest.getName().isEmpty() ?
                            userUpdateRequest.getLogin() : userUpdateRequest.getName());
            stmt.setString(2,userUpdateRequest.getLogin());
            stmt.setString(3,userUpdateRequest.getEmail());
            stmt.setDate(4, Date.valueOf(userUpdateRequest.getBirthday()));
            stmt.setLong(5, userUpdateRequest.getId());
            return stmt;
            },
                keyHolder
        );

        if (status == 1) {
            return UserMapper.fromUserToDTO(User.builder()
                    .id(Objects.requireNonNull(keyHolder.getKey()).longValue())
                    .name(userUpdateRequest.getName())
                    .login(userUpdateRequest.getLogin())
                    .email(userUpdateRequest.getEmail())
                    .birthday(userUpdateRequest.getBirthday())
                    .build());
        }

        throw  new UserNotFoundException("User with id "+userUpdateRequest+" not found");
    }

    @Override
    public UserDTO getUser(Long id) {
        String sql = "select * from users where user_id = ?;";
        UserDTO userDTO;

        try {
             userDTO = UserMapper.fromUserToDTO(jdbcTemplate.queryForObject(sql, this::mapRowToUser, id));
        } catch (EmptyResultDataAccessException e) {
            throw  new UserNotFoundException("User is not found");
        }

        return userDTO;
    }

    @Override
    public List<UserDTO> getAll() {
        String sql = "select * from users;";

        return UserMapper.fromUsersToDTOs(jdbcTemplate.query(sql, this::mapRowToUser));
    }

    @Override
    public User findById(Long id) {
        String sql = "select * from users where user_id = ?;";
        User user;

        try {
            user = jdbcTemplate.queryForObject(sql, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            throw  new UserNotFoundException("User with id "+id+" not found");
        }

        return user;
    }


    private List<Long> getFriends(Long id) {
        String sql = "select f.friend_id from friends as f " +
                "left join users as u on f.user_id = u.user_id " +
                "where f.user_id = ?";

        return jdbcTemplate.query(sql, this::mapRowToFriendId, id);
    }


    private Long mapRowToFriendId (ResultSet resultSet, int rowNum) throws SQLException {
        return resultSet.getLong("friend_id");
    }

    private User mapRowToUser(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .name(resultSet.getString("name"))
                .login(resultSet.getString("login"))
                .email(resultSet.getString("email"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friends(new HashSet<>(getFriends(resultSet.getLong("user_id"))))
                .build();
    }
}
