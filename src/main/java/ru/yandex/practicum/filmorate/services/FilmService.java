package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.requests.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.requests.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.utils.DateValidator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private List<Film> films;
    private int counter;

    public FilmService() {
        films = new ArrayList<>();
        counter = 1;
    }

    public FilmDTO add(FilmCreateRequest filmCreateRequest) {
        Film film = new Film();

        if (!DateValidator.isValidRelease(filmCreateRequest.getRelease())) {
            log.debug("Film has bad release date: " + filmCreateRequest.getRelease());
            return null;
        }

        film.setName(filmCreateRequest.getName());
        film.setDescription(filmCreateRequest.getDescription());
        film.setRelease(filmCreateRequest.getRelease());
        film.setDuration(filmCreateRequest.getDuration());

        film.setId(counter++);

        films.add(film);

        log.debug("Film with id " + film.getId() + " created");

        return FilmMapper.fromFilmToDTO(film);
    }

    public Film findById(int id) {
        return films.stream()
                .filter(film -> film.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public FilmDTO update(FilmUpdateRequest filmUpdateRequest) {
        Film film = findById(filmUpdateRequest.getId());

        if (film == null) {
            log.debug("film with id " + filmUpdateRequest.getId() + " not found");

            return null;
        }

        if (!DateValidator.isValidRelease(filmUpdateRequest.getRelease())) {
            log.debug("Film has bad release date: " + filmUpdateRequest.getRelease());
            return null;
        }

        film.setName(filmUpdateRequest.getName());
        film.setDescription(filmUpdateRequest.getDescription());
        film.setRelease(filmUpdateRequest.getRelease());
        film.setDuration(filmUpdateRequest.getDuration());

        films.add(film);

        log.debug("Film with id " + film.getId() + " updated");

        return FilmMapper.fromFilmToDTO(film);
    }

    public List<FilmDTO> getAll() {
        return FilmMapper.fromFilmsToDTOs(films);
    }

}
