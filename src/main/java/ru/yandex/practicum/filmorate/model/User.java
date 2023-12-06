package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@NotNull
public class User {
    private int id;
    private String name;
    @Past
    private LocalDate birthday;
    @Email
    private final String email;
    @NotBlank
    private final String login;
}
