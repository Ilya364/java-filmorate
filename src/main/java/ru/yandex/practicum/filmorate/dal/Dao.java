package ru.yandex.practicum.filmorate.dal;

import java.util.List;

public interface Dao<T> {
    T add(T o);

    T get(long id);

    void remove(long id);

    T update(T o);

    List<T> getAll();
}
