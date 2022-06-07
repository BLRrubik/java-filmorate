package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.Rating;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.requests.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.requests.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.requests.user.UserCreateRequest;
import ru.yandex.practicum.filmorate.requests.user.UserUpdateRequest;
import ru.yandex.practicum.filmorate.storage.film.DbFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.DbUserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase()
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmoRateApplicationTests {
    private final DbUserStorage userStorage;
    private final DbFilmStorage filmStorage;

    @Test
    public void shouldBeSizeOne() {

        List<UserDTO> users = userStorage.getAll();

        assertEquals(1, users.size());

    }

    //region User DB Storage Test
    @Test
    public void shouldGetUserById() {
        userStorage.add(new UserCreateRequest(
                "g@gmail.com",
                "1",
                "1",
                LocalDate.of(2002,7,3)
        ));


        Optional<User> userOptional = Optional.of(userStorage.findById(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void shouldUpdateUserById() {

        userStorage.update(new UserUpdateRequest(
                1L,
                "g@gmail.com",
                "2",
                "2",
                LocalDate.of(2002,7,3)
        ));


        Optional<User> userOptional = Optional.of(userStorage.findById(1L));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("name", "2")
                );
    }

    @Test
    public void shouldBeSizeZero() {

        List<FilmDTO> users = filmStorage.getAll();

        assertEquals(1, users.size());

    }

    //region User DB Storage Test
    @Test
    public void shouldGetFilmById() {
        filmStorage.add(new FilmCreateRequest(
                "1",
                "1",
                LocalDate.of(2002,7,3),
                (int) Duration.ofMinutes(100).toMinutes(),
                new ArrayList<>(),
                new Rating(1L, "a", "a")
        ));


        Optional<Film> filmOptional = Optional.of(filmStorage.findById(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void shouldUpdateFilmById() {

        filmStorage.add(new FilmCreateRequest(
                "1",
                "1",
                LocalDate.of(2002,7,3),
                (int) Duration.ofMinutes(100).toMinutes(),
                new ArrayList<>(),
                new Rating(1L, "a", "a")
        ));

        filmStorage.update(new FilmUpdateRequest(
                1L,
                "2",
                "2",
                LocalDate.of(2002,7,3),
                (int) Duration.ofMinutes(100).toMinutes(),
                new ArrayList<>(),
                new Rating(1L, "a", "a")
        ));


        Optional<Film> filmOptional = Optional.of(filmStorage.findById(1L));

        assertThat(filmOptional)
                .isPresent()
                .hasValueSatisfying(film ->
                        assertThat(film).hasFieldOrPropertyWithValue("name", "2")
                );
    }
}
