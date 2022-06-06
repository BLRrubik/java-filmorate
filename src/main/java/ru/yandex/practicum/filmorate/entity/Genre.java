package ru.yandex.practicum.filmorate.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Genre {
    private Long id;
    private String name;
}
