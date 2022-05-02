package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.exceptions.InvalidParamException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public FilmDTO addLike(Long id, Long userId) {
        Film film = filmStorage.findById(id);

        if (film == null) {
            throw new FilmNotFound("Film with id: " + id + " not found");
        }



        film.addLike(userId);

        return FilmMapper.fromFilmToDTO(film);
    }

    public FilmDTO deleteLike(Long id, Long userId) {
        Film film = filmStorage.findById(id);

        if (film == null) {
            throw new FilmNotFound("Film with id: " + id + " not found");
        }

        film.deleteLike(userId);

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

        return FilmMapper.fromFilmsToDTOs(popularity.stream().limit(count).collect(Collectors.toList()));
    }
}
