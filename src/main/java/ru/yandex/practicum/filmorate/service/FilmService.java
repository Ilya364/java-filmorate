package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage storage;
    private final UserStorage userStorage;
    private static long nextId = 0;

    @Autowired
    public FilmService(FilmStorage storage, UserStorage userStorage) {
        this.storage = storage;
        this.userStorage = userStorage;
    }

    public void add(Film film) {
        film.setId(++nextId);
        storage.add(film);
    }

    public void update(Film film) {
        storage.update(film);
    }

    public Film get(long filmId) {
        return storage.get(filmId);
    }

    public void remove(long filmId) {
        storage.remove(filmId);
    }

    public List<Film> getAll() {
        return new ArrayList<>(storage.getAll());
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
                .sorted(Comparator.comparing(film -> film.getLikes().size() * -1))
                .limit(count)
                .collect(Collectors.toList());
    }
}
