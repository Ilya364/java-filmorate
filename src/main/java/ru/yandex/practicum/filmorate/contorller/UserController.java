package ru.yandex.practicum.filmorate.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController implements Controller<User> {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public User add(@Valid @RequestBody User user) {
        service.add(user);
        log.info("Добавлен пользователь.");
        return user;
    }

    @Override
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        service.update(user);
        log.info("Пользователь с id = {} обновлен.", user.getId());
        return user;
    }

    @Override
    @GetMapping("/{id}")
    public User get(@PathVariable long id) {
        return service.get(id);
    }

    @Override
    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        service.addToFriends(id, friendId);
        log.info("Добавлен пользователь.");
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id, @PathVariable int friendId) {
        service.removeFromFriends(id, friendId);
        log.info("Пользователь с id = {} удален.", id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        return service.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return new ArrayList<>(service.getCommonFriends(id, otherId));
    }
}
