package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.BaseUnit;
import java.util.List;

public interface Dao<T extends BaseUnit> {
    T add(T o);

    T get(long id);

    void remove(long id);

    T update(T o);

    List<T> getAll();
}
