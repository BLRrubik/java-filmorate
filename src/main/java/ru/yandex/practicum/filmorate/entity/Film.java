package ru.yandex.practicum.filmorate.entity;

import lombok.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate release;
    private Duration duration;
    private Rating mpa;
    private Set<Long> likes = new HashSet<>();

    public void addLike(Long id) {
        likes.add(id);
    }

    public void deleteLike(Long id) {
        likes.remove(id);
    }
}
