package ru.yandex.practicum.filmorate.requests.film;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.yandex.practicum.filmorate.entity.Genre;
import ru.yandex.practicum.filmorate.entity.Rating;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmUpdateRequest {
    @NotNull
    private Long id;

    @NotEmpty
    @NotNull
    private String name;

    @Size(max = 200, message = "max length is 200 symbols")
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @NotNull
    @Positive
    private int duration;

    private List<Genre> genres = new ArrayList<>();

    @NotNull
    private Rating mpa;
}