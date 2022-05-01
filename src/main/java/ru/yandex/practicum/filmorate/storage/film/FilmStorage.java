package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.requests.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.requests.film.FilmUpdateRequest;

import java.util.List;

public interface FilmStorage {
    FilmDTO add(FilmCreateRequest filmCreateRequest);

    FilmDTO update(FilmUpdateRequest filmUpdateRequest);

    FilmDTO getFilm(Long id);

    List<FilmDTO> getAll();

    Film findById(Long id);

    List<Film> get();
}
