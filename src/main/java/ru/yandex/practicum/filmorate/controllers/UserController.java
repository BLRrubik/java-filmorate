package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.requests.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.requests.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getAll() {
        log.info("Request to get all users");

        return ResponseEntity.of(Optional.of(userService.getAll()));
    }

    @PostMapping("/create")
    public ResponseEntity<UserDTO> create(@Valid @RequestBody UserCreateRequest userCreateRequest) {
        log.info("Request to add user");

        return ResponseEntity.of(Optional.of(userService.add(userCreateRequest)));
    }

    @PostMapping("/update")
    public ResponseEntity<UserDTO> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        log.info("Request to update user");

        return ResponseEntity.of(Optional.of(userService.update(userUpdateRequest)));
    }
}
