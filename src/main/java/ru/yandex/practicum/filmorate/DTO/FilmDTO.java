package ru.yandex.practicum.filmorate.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilmDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate release;
    private int duration;
//    private Set<Long> likes;
}
