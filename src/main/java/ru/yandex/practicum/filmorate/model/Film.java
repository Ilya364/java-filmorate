package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@NotNull
public class Film {
    private int id;
    @NotBlank
    private final String name;
    @Size(max = 200, message = "{Максимальная длина описания - 200 символов.}")
    private final String description;
    @MinimumDate
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}
