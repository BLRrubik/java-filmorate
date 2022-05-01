package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.requests.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.services.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

public class UserValidationTest {
    private UserController userController;
    private Validator validator;

    @BeforeEach
    public void init() {
        userController = new UserController(new UserService());
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldBeOkWhenCorrectData() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("asd@gmail.com",
                "asd",
                "sad",
                LocalDate.now().minusDays(31));


        ResponseEntity<UserDTO> response = userController.create(userCreateRequest);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldBeFailWhenIncorrectEmail() {
        UserCreateRequest userCreateRequest = new UserCreateRequest("asd",
                "asd",
                "sad",
                LocalDate.now().minusDays(31));

        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(userCreateRequest);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldBeFailWhenIncorrectLogin() {

        UserCreateRequest userCreateRequest = new UserCreateRequest("asd",
                "",
                "sad",
                LocalDate.now().minusDays(31));

        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(userCreateRequest);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    public void shouldBeFailWhenIncorrectBirthday() {

        UserCreateRequest userCreateRequest = new UserCreateRequest("asd",
                "asd",
                "sad",
                LocalDate.now().plusDays(31));

        Set<ConstraintViolation<UserCreateRequest>> violations = validator.validate(userCreateRequest);
        Assertions.assertFalse(violations.isEmpty());
    }
}
