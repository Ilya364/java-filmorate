package ru.yandex.practicum.filmorate.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController extends Controller<User> {

    private void validate(User user) {
        final String name = user.getName();
        final String login = user.getLogin();
        if (login.contains(" ")) {
            log.warn("Попытка добавления пользователя с логином, содержащим пробел.");
            throw new ValidationException("Логин не может содержать пробел.");
        }
        if (name == null) {
            user.setName(user.getLogin());
        }
    }

    @Override
    @PostMapping
    public User add(@Valid @RequestBody User user) {
        validate(user);
        user.setId(nextId++);
        elements.put(user.getId(), user);
        log.info("Добавлен пользователь.");
        return user;
    }

    @Override
    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validate(user);
        if (elements.containsKey(user.getId())) {
            elements.put(user.getId(), user);
            log.warn("Пользователь с id = {} обновлен.", user.getId());
            return user;
        } else {
            log.error("Попытка обновления несуществующего пользователя");
            throw new NotFoundException("Пользователь с заданным id не найден.");
        }
    }

    @Override
    @GetMapping
    public List<User> getAll() {
        return super.getAll();
    }
}
