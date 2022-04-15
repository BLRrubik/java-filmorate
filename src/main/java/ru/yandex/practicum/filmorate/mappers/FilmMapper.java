package ru.yandex.practicum.filmorate.mappers;

import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.DTO.UserDTO;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class FilmMapper {
    public static FilmDTO fromFilmToDTO(Film film) {
        return new FilmDTO(
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getRelease(),
                film.getDuration()
        );
    }

    public static List<FilmDTO> fromFilmsToDTOs(List<Film> films) {
        return films.stream()
                .map(FilmMapper::fromFilmToDTO)
                .collect(Collectors.toList());
    }
}
