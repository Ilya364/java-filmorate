package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NotNull
public class Film {
    private long id;
    @NotBlank
    private final String name;
    @Size(max = 200, message = "{Максимальная длина описания - 200 символов.}")
    private final String description;
    @MinimumDate
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private final Set<Long> likes = new HashSet<>();

    public void addLike(long userId) {
        likes.add(userId);
    }

    public void removeLike(long userId) {
        likes.remove(userId);
    }
}
