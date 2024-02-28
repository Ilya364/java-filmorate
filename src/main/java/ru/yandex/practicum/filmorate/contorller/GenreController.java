package ru.yandex.practicum.filmorate.contorller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController extends Controller<GenreService, Genre> {
    @GetMapping("{id}")
    public BaseUnit get(@PathVariable short id) {
        return super.get(id);
    }

    @GetMapping
    public List<BaseUnit> getAll() {
        return super.getAll();
    }
}
