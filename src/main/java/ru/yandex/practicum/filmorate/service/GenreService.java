package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

@Service
@AllArgsConstructor
public class GenreService extends AbstractService<GenreDao, Genre> {
    private final GenreDao dao;
}
