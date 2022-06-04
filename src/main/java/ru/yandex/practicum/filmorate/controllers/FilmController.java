package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.requests.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.requests.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(@Qualifier("db_film_storage") FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("")
    public ResponseEntity<List<FilmDTO>> getAll() {
        return ResponseEntity.of(Optional.of(filmStorage.getAll()));
    }

    @GetMapping("{id}")
    public ResponseEntity<FilmDTO> getFilm(@PathVariable Long id) {
        return ResponseEntity.of(Optional.of(filmStorage.getFilm(id)));
    }

    @PostMapping("")
    public ResponseEntity<FilmDTO> create(@Valid @RequestBody FilmCreateRequest filmCreateRequest) {
        return ResponseEntity.of(Optional.of(filmStorage.add(filmCreateRequest)));
    }

    @PutMapping("")
    public ResponseEntity<FilmDTO> update(@Valid @RequestBody FilmUpdateRequest filmUpdateRequest) {
        return ResponseEntity.of(Optional.of(filmStorage.update(filmUpdateRequest)));
    }

    @PutMapping("{id}/like/{userId}")
    public ResponseEntity<FilmDTO> addLike(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.of(Optional.of(filmService.addLike(id, userId)));
    }

    @DeleteMapping("{id}/like/{userId}")
    public ResponseEntity<FilmDTO> deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return ResponseEntity.of(Optional.of(filmService.deleteLike(id, userId)));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<FilmDTO>> getPopular(@RequestParam(name= "count",
            required = true,
            defaultValue = "10") String count) {
        return ResponseEntity.of(Optional.of(filmService.getPopular(count)));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public void exception(){

    }
}
