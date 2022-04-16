package ru.yandex.practicum.filmorate.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.DTO.FilmDTO;
import ru.yandex.practicum.filmorate.requests.film.FilmCreateRequest;
import ru.yandex.practicum.filmorate.requests.film.FilmUpdateRequest;
import ru.yandex.practicum.filmorate.services.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/film")
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping("")
    public ResponseEntity<List<FilmDTO>> getAll() {
        return ResponseEntity.of(Optional.of(filmService.getAll()));
    }

    @PostMapping("")
    public ResponseEntity<FilmDTO> create(@Valid @RequestBody FilmCreateRequest filmCreateRequest) {
        return ResponseEntity.of(Optional.of(filmService.add(filmCreateRequest)));
    }

    @PutMapping("")
    public ResponseEntity<FilmDTO> update(@Valid @RequestBody FilmUpdateRequest filmUpdateRequest) {
        return ResponseEntity.of(Optional.of(filmService.update(filmUpdateRequest)));
    }
}
