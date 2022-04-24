package ru.yandex.practicum.filmorate.utils;

import java.time.LocalDate;

public class DateValidator {
    public static boolean isValidRelease(LocalDate localDate) {
        return localDate.isBefore(LocalDate.of(1895,12,28));
    }

    public static boolean isValidBirthday(LocalDate localDate) {
        return localDate.isBefore(LocalDate.now());
    }
}
