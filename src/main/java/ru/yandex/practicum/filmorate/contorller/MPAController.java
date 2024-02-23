package ru.yandex.practicum.filmorate.contorller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;
import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MPAController extends Controller<MPAService, MPA> {
    @Autowired
    public MPAController(MPAService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public MPA get(@PathVariable short id) {
        return super.get(id);
    }

    @GetMapping
    public List<MPA> getAll() {
        return super.getAll();
    }
}
