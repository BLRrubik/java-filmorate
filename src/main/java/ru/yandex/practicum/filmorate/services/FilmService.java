package ru.yandex.practicum.filmorate.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.exceptions.InvalidParamException;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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

        List<FilmDTO> popularity = filmStorage.getAll();
        popularity.sort(Comparator.comparingInt(film -> film.getLikes().size()));

        return popularity.stream().limit(count).collect(Collectors.toList());
    }
}
