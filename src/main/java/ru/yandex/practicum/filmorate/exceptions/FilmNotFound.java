package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FilmNotFound extends RuntimeException{
    public FilmNotFound() {
    }

    public FilmNotFound(String message) {
        super(message);
    }
}
