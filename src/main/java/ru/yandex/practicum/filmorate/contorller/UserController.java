package ru.yandex.practicum.filmorate.contorller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@SuppressWarnings("checkstyle:Regexp")
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private static int nextId = 1;
    
    private void validate(User user) {
        final String name = user.getName();
        final LocalDate birthday = user.getBirthday();
        final LocalDate today = LocalDate.now();
        if (birthday.isAfter(today)) {
            log.warn("Попытка добавления пользователя с неправильной датой рождения.");
            throw new ValidationException("День рождения пользователя не может быть в будущем.");
        }
        if (name == null) {
            user.setName(user.getLogin());
        }
    }
    
    @PostMapping
    @ResponseBody
    public User addUser(@Valid @RequestBody User user) {
        validate(user);
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Добавлен пользователь.");
        return user;
    }
    
    @PutMapping
    @ResponseBody
    public User updateUser(@Valid @RequestBody User user) {
        validate(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.warn("Пользователь с id = " + user.getId() + " обновлен.");
            return user;
        } else {
            log.error("Попытка обновления несуществующего пользователя");
            throw new NotFoundException("Пользователь с заданным id не найден.");
        }
    }
    
    @GetMapping
    @ResponseBody
    public ArrayList<User> getUsers() {
        return new ArrayList<>(users.values());
    }
}
