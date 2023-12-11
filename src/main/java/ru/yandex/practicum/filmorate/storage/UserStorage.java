package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User get(long id);

    void add(User user);

    void update(User user);

    void remove(long id);

    List<User> getAll();
}
