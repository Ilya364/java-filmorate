package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.dal.Dao;
import java.util.List;

public abstract class AbstractService<T extends Dao, V> {
    @Autowired
    protected T dao;

    public void add(V o) {
        dao.add(o);
    }

    public V get(long id) {
        return (V)dao.get(id);
    }

    public V update(V o) {
        return (V) dao.update(o);
    }

    public void remove(long id) {
        dao.remove(id);
    }

    public List<V> getAll() {
        return dao.getAll();
    }
}
