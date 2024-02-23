package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

public interface FilmDao extends Dao<Film> {
    void addLike(long userId, long filmId);

    void deleteLike(long userId, long filmId);

    List<Film> getMostPopular(long count);
}
