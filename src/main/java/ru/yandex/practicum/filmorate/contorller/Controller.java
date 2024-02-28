package ru.yandex.practicum.filmorate.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.service.AbstractService;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class Controller<T extends AbstractService, V extends BaseUnit> {
    @Autowired
    protected T service;

    public BaseUnit add(V o) {
        service.add(o);
        log.info("{} добавлен.", o.getClass().getName());
        return o;
    }

    public BaseUnit update(V o) {
        BaseUnit updated = service.update(o);
        log.info("{} обновлен.", o.getClass().getName());
        return updated;
    }

    public BaseUnit get(long id) {
        BaseUnit o = service.get(id);
        log.info("{} получен.", o.getClass().getName());
        return o;
    }

    public List<BaseUnit> getAll() {
        List all = service.getAll();
        log.info("Получен список всех элементов.");
        return new ArrayList<>(all);
    }
}
