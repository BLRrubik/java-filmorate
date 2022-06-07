package ru.yandex.practicum.filmorate.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Rating {
    private Long id;
    String name;
    String description;
}
