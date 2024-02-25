package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.MinimumDate;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
@NotNull
@Builder
public class Film extends BaseUnit {
    private long id;
    @NotBlank
    private final String name;
    private final Set<Genre> genres = new HashSet<>();
    @Size(max = 200, message = "{Максимальная длина описания - 200 символов.}")
    private final String description;
    @MinimumDate
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private final MPA mpa;
    private int rate;

    public void addLike(long userId) {
        rate++;
    }

    public void removeLike(long userId) {
        rate--;
    }

    public void addAllGenres(Collection<Genre> genres) {
        this.genres.addAll(genres);
    }

    public void addGenre(Genre genre) {
        this.genres.add(genre);
    }
}
