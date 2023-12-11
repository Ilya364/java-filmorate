package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film get(long id);

    List<Film> getAll();

    void add(Film film);

    void update(Film film);

    void remove(long id);
}
