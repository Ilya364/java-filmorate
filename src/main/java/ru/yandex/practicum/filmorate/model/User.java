package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NotNull
public class User {
    private int id;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    @Email
    private final String email;
    @NotBlank
    private final String login;
}
