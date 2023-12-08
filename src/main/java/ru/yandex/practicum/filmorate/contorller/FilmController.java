package ru.yandex.practicum.filmorate.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends Controller<Film> {
    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        film.setId(nextId++);
        elements.put(film.getId(), film);
        log.info("Добавлен фильм.");
        return film;
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        if (elements.containsKey(film.getId())) {
            elements.put(film.getId(), film);
            log.info("Фильм с id = {} обновлен.", film.getId());
            return film;
        } else {
            log.warn("Попытка обновления несуществующего фильма.");
            throw new NotFoundException("Фильм с заданным id не найден.");
        }
    }

    @Override
    @GetMapping
    public List<Film> getAll() {
        return super.getAll();
    }
}
