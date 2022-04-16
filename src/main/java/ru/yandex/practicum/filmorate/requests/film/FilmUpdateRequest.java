package ru.yandex.practicum.filmorate.requests.film;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmUpdateRequest {
    @NotEmpty
    @NotNull
    @Positive
    private int id;

    @NotEmpty
    @NotNull
    private String name;

    @Size(max = 200, message = "max length is 200 symbols")
    private String description;

    @NotNull
    private LocalDate release;

    @NotEmpty
    @NotNull
    @Positive
    private int duration;
}