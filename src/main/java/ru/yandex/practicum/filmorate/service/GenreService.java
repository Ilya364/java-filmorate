package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

@Service
public class GenreService extends AbstractService<GenreDao, Genre> {
}
