package ru.yandex.practicum.filmorate.contorller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Controller<T> {
    protected Map<Integer, T> elements = new HashMap<>();
    protected int nextId = 1;

    public abstract T add(T element);

    public abstract T update(T element);

    public List<T> getAll() {
        return new ArrayList<>(elements.values());
    }
}
