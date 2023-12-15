package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.Storage;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService extends AbstractService<Film> {
    private static final Comparator<Film> BY_LIKES = Comparator.comparing(film -> film.getLikes().size() * -1);
    private final Storage<User> userStorage;
    private static long nextId = 0;

    @Autowired
    public FilmService(InMemoryFilmStorage storage, InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
        this.storage = storage;
    }

    public void add(Film film) {
        film.setId(++nextId);
        storage.add(film.getId(), film);
    }

    public void update(Film film) {
        storage.update(film.getId(), film);
    }

    public void addLike(long filmId, long userId) {
        Film film = storage.get(filmId);
        userStorage.get(userId);
        film.addLike(userId);
    }

    public void deleteLike(long filmId, long userId) {
        Film film = storage.get(filmId);
        userStorage.get(userId);
        film.removeLike(userId);
    }

    public List<Film> getMostPopular(long count) {
        return storage.getAll().stream()
                .sorted(BY_LIKES)
                .limit(count)
                .collect(Collectors.toList());
    }
}
