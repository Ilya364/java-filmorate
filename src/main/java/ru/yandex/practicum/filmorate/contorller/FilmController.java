package ru.yandex.practicum.filmorate.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("checkstyle:Regexp")
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int nextId = 1;

    private void validate(Film film) {
        final LocalDate date = film.getReleaseDate();
        final LocalDate movieBirthday = LocalDate.of(1895, Month.DECEMBER, 28);
        if ((date.isBefore(movieBirthday))) {
            log.warn("Попытка добавления фильма с слишком ранней датой релиза.");
            throw new ValidationException("Некорректная дата релиза.");
        }
    }

    @PostMapping
    @ResponseBody
    public Film addFilm(@Valid @RequestBody Film film) {
        validate(film);
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Добавлен фильм.");
        return film;
    }

    @PutMapping
    @ResponseBody
    public Film updateFilm(@Valid @RequestBody Film film) {
        validate(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм с id = " + film.getId() + " обновлен.");
            return film;
        } else {
            log.warn("Попытка обновления несуществующего фильма.");
            throw new NotFoundException("Фильм с заданным id не найден.");
        }
    }

    @GetMapping
    @ResponseBody
    public ArrayList<Film> getFilms() {
        return new ArrayList<>(films.values());
    }
}
