package ru.yandex.practicum.filmorate.contorller;

import java.util.List;

public interface Controller<V> {

    V add(V element);

    V update(V element);

    V get(long id);

    List<V> getAll();
}
