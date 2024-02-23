package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Storage<T> {
    private final Map<Long, T> elements = new HashMap<>();

    public void add(long elementId, T element) {
        elements.put(elementId, element);
    }

    public void update(long elementId, T element) {
        if (!elements.containsKey(elementId)) {
            throw new NotFoundException("Элемент с id = " + elementId + " не найден.");
        }
        elements.put(elementId, element);
    }

    public T get(long id) {
        T element = elements.get(id);
        if (element == null) {
            throw new NotFoundException("Элемент с id = " + id + " не обнаружен.");
        }
        return element;
    }

    public void remove(long id) {
        T element = elements.get(id);
        if (element == null) {
            throw new NotFoundException("Элемент с id = " + id + " не найден.");
        }
        elements.remove(id);
    }

    public List<T> getAll() {
        return new ArrayList<>(elements.values());
    }
}
