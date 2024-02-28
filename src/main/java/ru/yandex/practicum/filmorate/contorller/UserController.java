package ru.yandex.practicum.filmorate.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends Controller<UserService, User> {
    @Override
    @PostMapping
    public BaseUnit add(@Valid @RequestBody User user) {
        return super.add(user);
    }

    @Override
    @PutMapping
    public BaseUnit update(@Valid @RequestBody User o) {
        return super.update(o);
    }

    @Override
    @GetMapping("/{id}")
    public BaseUnit get(@PathVariable long id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    public List<BaseUnit> getAll() {
        return super.getAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        service.addToFriends(id, friendId);
        log.info("Добавлен пользователь.");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        service.removeFromFriends(id, friendId);
        log.info("Пользователи с id  = {} и {} удалили друг друга из друзей.", id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        List<User> friends = service.getUserFriends(id);
        log.info("Получен список друзей пользователя с id = {}", id);
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        List<User> commonFriends = new ArrayList<>(service.getCommonFriends(id, otherId));
        log.info("Получен список общих друзей для пользователей с id = {} и {}", id, otherId);
        return commonFriends;
    }
}
