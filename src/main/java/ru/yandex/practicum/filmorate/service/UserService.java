package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage storage;
    private static long nextId = 0;

    private void validate(User user) {
        final String name = user.getName();
        final String login = user.getLogin();
        if (login.contains(" ")) {
            log.warn("Попытка добавления пользователя с логином, содержащим пробел.");
            throw new ValidationException("Логин не может содержать пробел.");
        }
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
        }
    }

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    public void add(User user) {
        validate(user);
        user.setId(++nextId);
        storage.add(user);
    }

    public void update(User user) {
        validate(user);
        storage.update(user);
    }

    public User get(long userId) {
        return storage.get(userId);
    }

    public void remove(long userId) {
        storage.remove(userId);
    }

    public List<User> getAll() {
        return storage.getAll();
    }

    public List<User> getUserFriends(int userId) {
        User user = storage.get(userId);
        List<User> friends = new ArrayList<>();
        for (long friendId: user.getFriends()) {
            friends.add(storage.get(friendId));
        }
        return friends;
    }

    public void addToFriends(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
    }

    public void removeFromFriends(long userId, long friendId) {
        User user = storage.get(userId);
        User friend = storage.get(friendId);
        if (user == null) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден.");
        }
        if (friend == null) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден.");
        }
        user.removeFriend(friendId);
        friend.removeFriend(userId);
    }

    public Set<User> getCommonFriends(long userId, long otherId) {
        User user = storage.get(userId);
        User other = storage.get(otherId);
        Set<User> commonFriends = new HashSet<>();
        for (Long friendId: user.getFriends()) {
            if (other.getFriends().contains(friendId)) {
                commonFriends.add(storage.get(friendId));
            }
        }
        return commonFriends;
    }
}
