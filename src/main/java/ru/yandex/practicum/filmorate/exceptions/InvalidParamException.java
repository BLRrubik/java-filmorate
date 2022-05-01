package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidParamException extends RuntimeException{
    public InvalidParamException() {
    }

    public InvalidParamException(String message) {
        super(message);
    }
}
