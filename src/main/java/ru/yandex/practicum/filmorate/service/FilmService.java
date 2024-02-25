package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.FilmDao;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

@Service
public class FilmService extends AbstractService<FilmDao, Film> {
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
