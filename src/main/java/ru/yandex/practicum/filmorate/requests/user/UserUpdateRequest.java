package ru.yandex.practicum.filmorate.requests.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.implementation.bind.annotation.Empty;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    @NotNull
    private int id;

    @Email
    private String email;

    @NotEmpty
    @NotNull
    private String login;

    private String name;

    @NotNull
    private LocalDate birthday;
}
