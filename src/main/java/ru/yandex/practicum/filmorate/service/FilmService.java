package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmDao;
import ru.yandex.practicum.filmorate.dal.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
public class FilmService extends AbstractService<FilmDao, Film> {
    private static final Comparator<Film> BY_LIKES = Comparator.comparing(film -> film.getRate() * -1);
    private final UserDao userDao;

    public void addLike(long filmId, long userId) {
        dao.addLike(userId, filmId);
    }

    public void deleteLike(long filmId, long userId) {
        dao.deleteLike(userId, filmId);
    }

    public List<Film> getMostPopular(long count) {
        return dao.getMostPopular(count);
    }
}
