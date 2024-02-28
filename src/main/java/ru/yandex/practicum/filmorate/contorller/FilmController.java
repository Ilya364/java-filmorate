package ru.yandex.practicum.filmorate.contorller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends Controller<FilmService, Film> {
    @Override
    @PostMapping
    public BaseUnit add(@Valid @RequestBody Film film) {
        return super.add(film);
    }

    @Override
    @PutMapping
    public BaseUnit update(@Valid @RequestBody Film o) {
        return super.update(o);
    }

    @Override
    @GetMapping("{id}")
    public BaseUnit get(@PathVariable long id) {
        return super.get(id);
    }

    @Override
    @GetMapping
    public List<BaseUnit> getAll() {
        return super.getAll();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable long id, @PathVariable long userId) {
        service.addLike(id, userId);
        log.info("Пользователь с id = {} поставил лайк фильму с id = {}.", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable long id, @PathVariable long userId) {
        service.deleteLike(id, userId);
        log.info("Пользователь с id = {} удалил лайк фильму с id = {}.", userId, id);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopular(@RequestParam(defaultValue = "10") int count) {
        List<Film> popularFilms = service.getMostPopular(count);
        log.info("Получен список из {} самых популярных фильмов.", count);
        return popularFilms;
    }
}
