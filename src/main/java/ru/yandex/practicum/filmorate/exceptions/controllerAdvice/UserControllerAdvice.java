package ru.yandex.practicum.filmorate.exceptions.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;

@ControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleException(UserNotFoundException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
