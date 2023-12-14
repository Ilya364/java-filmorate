package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService extends AbstractService<User> {
    private static long nextId = 0;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    private void validate(User user) {
        final String name = user.getName();
        final String login = user.getLogin();
        if (login.contains(" ")) {
            log.warn("Попытка добавления пользователя с логином, содержащим пробел.");
            throw new ValidationException("Логин не может содержать пробел.");
        }
        if (name == null || name.isEmpty()) {
            user.setName(user.getLogin());
        }
    }

    @Override
    public void add(User user) {
        validate(user);
        user.setId(++nextId);
        storage.add(user.getId(), user);
    }

    @Override
    public void update(User user) {
        validate(user);
        storage.update(user.getId(), user);
    }

    public List<User> getUserFriends(int userId) {
        User user = storage.get(userId);
        return user.getFriends().stream()
                .map(storage::get)
                .collect(Collectors.toList());
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

    public List<User> getCommonFriends(long userId, long otherId) {
        Set<Long> userFriends = storage.get(userId).getFriends();
        Set<Long> otherFriends = storage.get(otherId).getFriends();
        return userFriends.stream()
                .filter(otherFriends::contains)
                .map(storage::get)
                .collect(Collectors.toList());
    }
}
