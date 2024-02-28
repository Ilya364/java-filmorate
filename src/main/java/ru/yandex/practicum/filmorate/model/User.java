package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class User extends BaseUnit {
    private long id;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
    @Email
    private final String email;
    @NotBlank
    private final String login;
}
