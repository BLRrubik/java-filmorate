package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.requests.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.requests.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(@Qualifier("db_user_storage") UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAll() {
        log.info("Request to get all users");

        return ResponseEntity.of(Optional.of(userStorage.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        return ResponseEntity.of(Optional.of(userStorage.getUser(id)));
    }

    @PostMapping("")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        log.info("Request to add user");

        return ResponseEntity.of(Optional.of(userStorage.add(userCreateRequest)));
    }

    @PutMapping("")
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("Request to update user");

        return ResponseEntity.of(Optional.of(userStorage.update(userUpdateRequest)));
    }

    @PutMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDTO> addFriend(@PathVariable Long id,
                                             @PathVariable Long friendId) {
        return ResponseEntity.of(Optional.of(userService.addFriend(id, friendId)));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public ResponseEntity<UserDTO> deleteFriend(@PathVariable Long id,
                                                @PathVariable Long friendId) {
        return ResponseEntity.of(Optional.of(userService.deleteFriend(id, friendId)));
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<UserDTO>> getCommonFriends(@PathVariable Long id,
                                                          @PathVariable Long otherId) {
        return ResponseEntity.of(Optional.of(userService.getCommonFriends(id, otherId)));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserDTO>> getFriends(@PathVariable Long id) {

        return ResponseEntity.of(Optional.of(userService.getFriends(id)));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public void exception(){
        
    }
}
