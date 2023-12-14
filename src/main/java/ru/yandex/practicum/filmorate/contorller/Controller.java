package ru.yandex.practicum.filmorate.contorller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.AbstractService;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class Controller<T extends AbstractService<V>, V> {
    protected T service;

    public V add(V element) {
        service.add(element);
        log.info("{} добавлен.", element.getClass().getName());
        return element;
    }

    public V update(V element) {
        service.update(element);
        log.info("{} обновлен.", element.getClass().getName());
        return element;
    }

    public V get(long id) {
        V o = service.get(id);
        log.info("{} получен.", o.getClass().getName());
        return service.get(id);
    }

    public List<V> getAll() {
        List<V> all = service.getAll();
        log.info("Получен список всех элементов.");
        return new ArrayList<>(service.getAll());
    }
}
