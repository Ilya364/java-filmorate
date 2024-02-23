package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@Service
@Slf4j
public class UserService extends AbstractService<UserDao, User> {
    private static long nextId = 0;

    @Autowired
    public UserService(@Qualifier("UserDaoImpl") UserDao userDao) {
        dao = userDao;
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
        super.add(user);
    }

    @Override
    public User update(User user) {
        validate(user);
        return super.update(user);
    }

    public List<User> getUserFriends(long userId) {
        return dao.getUserFriends(userId);
    }

    public void addToFriends(long userId, long friendId) {
        dao.addToFriends(userId, friendId);
    }

    public void removeFromFriends(long userId, long friendId) {
        dao.removeFromFriends(userId, friendId);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        return dao.getCommonFriends(userId, otherId);
    }
}
