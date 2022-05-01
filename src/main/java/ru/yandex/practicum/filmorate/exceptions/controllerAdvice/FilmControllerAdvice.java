package ru.yandex.practicum.filmorate.exceptions.controllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.exceptions.InvalidParamException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;

@ControllerAdvice
public class FilmControllerAdvice {
    @ExceptionHandler(FilmNotFound.class)
    public ResponseEntity<String> notFoundException(FilmNotFound e) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidParamException.class)
    public ResponseEntity<String> invalidParamException(InvalidParamException e) {
        String message = e.getMessage();
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
