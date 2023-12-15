package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.storage.Storage;
import java.util.List;

public abstract class AbstractService<T> {
    protected Storage<T> storage;

    public abstract void add(T element);

    public abstract void update(T element);

    public T get(long id) {
        return storage.get(id);
    }

    public void remove(long id) {
        storage.remove(id);
    }

    public List<T> getAll() {
        return storage.getAll();
    }
}
