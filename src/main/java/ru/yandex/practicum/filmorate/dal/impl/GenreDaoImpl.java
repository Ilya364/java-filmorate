package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.GenreDao;
import ru.yandex.practicum.filmorate.dal.mapper.GenreMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;

@Component
@AllArgsConstructor
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate template;

    @Override
    public Genre get(long id) {
        String sqlQuery = "SELECT * " +
                "FROM genres " +
                "WHERE id = ?;";
        try {
            return template.queryForObject(sqlQuery, GenreMapper.genreRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанра с таким id не существует.");
        }
    }

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT * FROM genres " +
                "ORDER BY id ASC;";
        return template.query(sqlQuery, GenreMapper.genreRowMapper());
    }

    @Override
    public Genre add(Genre o) {
        return null; //просто заглушки, т.к. в тз не требуются
    }

    @Override
    public void remove(long id) {
    }

    @Override
    public Genre update(Genre o) {
        return null;
    }
}
