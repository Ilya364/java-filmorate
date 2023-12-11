package ru.yandex.practicum.filmorate.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController implements Controller<Film> {
    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @Override
    @PostMapping
    public Film add(@Valid @RequestBody Film film) {
        service.add(film);
        log.info("Добавлен фильм.");
        return film;
    }

    @Override
    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        service.update(film);
        log.info("Фильм с id = {} обновлен.", film.getId());
        return film;
    }

    @Override
    @GetMapping("{id}")
    public Film get(@PathVariable long id) {
        return service.get(id);
    }

    @Override
    @GetMapping
    public List<Film> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        service.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopular(@RequestParam(defaultValue = "10", required = false) int count) {
        return service.getMostPopular(count);
    }
}
