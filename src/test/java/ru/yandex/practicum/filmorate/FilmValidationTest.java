package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.requests.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class FilmValidationTest {
    private FilmController filmController;
    private Validator validator;

    @BeforeEach
    public void init() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        filmController = new FilmController(filmStorage, new FilmService(filmStorage,userStorage));
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldBeOkWhenCorrectData() {
        FilmCreateRequest filmCreateRequest = new FilmCreateRequest("asd",
                "asd",
                LocalDate.now(),
                100);


        ResponseEntity<FilmDTO> response = filmController.create(filmCreateRequest);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldBeFailWhenIncorrectName() {

        FilmCreateRequest filmCreateRequest = new FilmCreateRequest("",
                "asd",
                LocalDate.now(),
                100);

        Set<ConstraintViolation<FilmCreateRequest>> violations = validator.validate(filmCreateRequest);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldBeFailWhenIncorrectDescription() {

        FilmCreateRequest filmCreateRequest = new FilmCreateRequest("sasd",
                "sadasdas",
                LocalDate.now(),
                100);

        Set<ConstraintViolation<FilmCreateRequest>> violations = validator.validate(filmCreateRequest);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldBeFailWhenIncorrectReleaseDate() {

        FilmCreateRequest filmCreateRequest = new FilmCreateRequest("sasd",
                "sadasdas",
                LocalDate.of(2000, 1,1),
                100);

        Set<ConstraintViolation<FilmCreateRequest>> violations = validator.validate(filmCreateRequest);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldBeFailWhenIncorrectDuration() {

        FilmCreateRequest filmCreateRequest = new FilmCreateRequest("sasd",
                "sadasdas",
                LocalDate.of(1500, 1,1),
                -100);

        Set<ConstraintViolation<FilmCreateRequest>> violations = validator.validate(filmCreateRequest);
        Assertions.assertFalse(violations.isEmpty());
    }
}
