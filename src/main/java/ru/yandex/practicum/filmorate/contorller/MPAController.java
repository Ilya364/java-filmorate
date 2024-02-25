package ru.yandex.practicum.filmorate.contorller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.BaseUnit;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;
import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MPAController extends Controller<MPAService, MPA> {
    @GetMapping("/{id}")
    public BaseUnit get(@PathVariable short id) {
        return super.get(id);
    }

    @GetMapping
    public List<BaseUnit> getAll() {
        return super.getAll();
    }
}
