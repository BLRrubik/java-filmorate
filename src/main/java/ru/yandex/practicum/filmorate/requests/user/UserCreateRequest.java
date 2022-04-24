package ru.yandex.practicum.filmorate.requests.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    @Email
    private String email;

    @NotEmpty
    @NotNull
    private String login;

    private String name;

    @NotNull
    private LocalDate birthday;
}
