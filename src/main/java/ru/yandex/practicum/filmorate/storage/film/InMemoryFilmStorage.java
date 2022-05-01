package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.entity.Film;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFound;
import ru.yandex.practicum.filmorate.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.requests.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.requests.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.utils.DateValidator;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private List<Film> films;
    private Long counter;

    public InMemoryFilmStorage() {
        films = new ArrayList<>();
        counter = 1L;
    }

    @Override
    public FilmDTO add(FilmCreateRequest filmCreateRequest) {
        Film film = new Film();

        if (!DateValidator.isValidRelease(filmCreateRequest.getReleaseDate())) {
            log.debug("Film has bad release date: " + filmCreateRequest.getReleaseDate());
            return null;
        }

        if (filmCreateRequest.getDuration() <= 0) {

            return null;
        }

        film.setName(filmCreateRequest.getName());
        film.setDescription(filmCreateRequest.getDescription());
        film.setRelease(filmCreateRequest.getReleaseDate());
        film.setDuration(Duration.ofMinutes(filmCreateRequest.getDuration()));

        film.setId(counter++);

        films.add(film);

        log.debug("Film with id " + film.getId() + " created");

        return FilmMapper.fromFilmToDTO(film);
    }

    @Override
    public Film findById(Long id) {
        return films.stream()
                .filter(film -> film.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Film> get() {
        return films;
    }

    @Override
    public FilmDTO update(FilmUpdateRequest filmUpdateRequest) {
        Film film = findById(filmUpdateRequest.getId());

        if (film == null) {
            throw new FilmNotFound("Film with id: " + filmUpdateRequest.getId() + " not found");
        }

        if (!DateValidator.isValidRelease(filmUpdateRequest.getReleaseDate())) {
            log.debug("Film has bad release date: " + filmUpdateRequest.getReleaseDate());
            return null;
        }

        film.setName(filmUpdateRequest.getName());
        film.setDescription(filmUpdateRequest.getDescription());
        film.setRelease(filmUpdateRequest.getReleaseDate());
        film.setDuration(Duration.ofMinutes(filmUpdateRequest.getDuration()));

        films.add(film);

        log.debug("Film with id " + film.getId() + " updated");

        return FilmMapper.fromFilmToDTO(film);
    }

    @Override
    public FilmDTO getFilm(Long id) {
        Film film = findById(id);

        if (film == null) {
            throw new FilmNotFound("Film with id: " + id + " not found");
        }

        return FilmMapper.fromFilmToDTO(film);
    }

    @Override
    public List<FilmDTO> getAll() {
        return FilmMapper.fromFilmsToDTOs(films);
    }
}
