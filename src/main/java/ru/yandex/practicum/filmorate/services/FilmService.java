package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.User;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.exceptions.InvalidParamException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.requests.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.requests.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utils.DateValidator;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmService(@Qualifier("db_film_storage") FilmStorage filmStorage, @Qualifier("db_user_storage") UserStorage userStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FilmDTO> getAll() {
        return filmStorage.getAll();
    }

    public FilmDTO getFilm(Long id) {
        return filmStorage.getFilm(id);
    }

    public FilmDTO addFilm(FilmCreateRequest filmCreateRequest) {

        if (!DateValidator.isValidRelease(filmCreateRequest.getReleaseDate())) {
            return null;
        }

        return filmStorage.add(filmCreateRequest);
    }

    public FilmDTO updateFilm(FilmUpdateRequest filmUpdateRequest) {
        if (!DateValidator.isValidRelease(filmUpdateRequest.getReleaseDate())) {
            return null;
        }

        return filmStorage.update(filmUpdateRequest);
    }

    public FilmDTO addLike(Long id, Long userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);

        film.addLike(user.getId());

        String sql = "insert into likes (user_id, film_id) values(?,?)";

        jdbcTemplate.update(sql, user.getId(), film.getId());

        return FilmMapper.fromFilmToDTO(film);
    }

    public FilmDTO deleteLike(Long id, Long userId) {
        Film film = filmStorage.findById(id);
        User user = userStorage.findById(userId);

        film.deleteLike(userId);

        String sql = "delete from likes where film_id = ? and user_id = ?;";

        jdbcTemplate.update(sql, film.getId(), user.getId());

        return FilmMapper.fromFilmToDTO(film);
    }

    public List<FilmDTO> getPopular(String countString) {
        int count;

        try {
            count = Integer.parseInt(countString);
        } catch (Exception e){
            throw new InvalidParamException("Param is no valid");
        }

        List<Film> popularity = filmStorage.get();
        popularity.sort(Comparator.comparingInt(film -> film.getLikes().size()));
        Collections.reverse(popularity);

        return FilmMapper.fromFilmsToDTOs(popularity.stream().limit(count).collect(Collectors.toList()));
    }
}
