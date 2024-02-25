package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.Dao;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import java.util.List;

public abstract class AbstractService<T extends Dao, V extends BaseUnit> {
    @Autowired
    protected T dao;

    public void add(V o) {
        dao.add(o);
    }

    public BaseUnit get(long id) {
        return dao.get(id);
    }

    public BaseUnit update(V o) {
        return dao.update(o);
    }

    public void remove(long id) {
        dao.remove(id);
    }

    public List<BaseUnit> getAll() {
        return dao.getAll();
    }
}
