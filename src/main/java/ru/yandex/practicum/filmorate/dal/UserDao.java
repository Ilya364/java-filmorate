package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserDao extends Dao<User> {
    List<User> getUserFriends(long userId);

    void addToFriends(long userId, long friendId);

    void removeFromFriends(long userId, long friendId);

    List<User> getCommonFriends(long userId, long otherId);
}
