package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.MPADao;
import ru.yandex.practicum.filmorate.dal.mapper.MpaMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import java.util.List;

@Component
@AllArgsConstructor
public class MPADaoImpl implements MPADao {
    private final JdbcTemplate template;

    @Override
    public MPA get(long id) {
        String sqlQuery = "SELECT * " +
                "FROM rating_mpa " +
                "WHERE id = ?;";
        try {
            return template.queryForObject(sqlQuery, MpaMapper.mpaRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Рейтинга с таким id не существует.");
        }
    }

    @Override
    public List<MPA> getAll() {
        String sqlQuery = "SELECT * FROM rating_mpa " +
                "ORDER BY id ASC;";
        return template.query(sqlQuery, MpaMapper.mpaRowMapper());
    }

    @Override
    public MPA add(MPA o) {
        return null; //просто заглушки, т.к. в тз не требуются
    }

    @Override
    public void remove(long id) {
    }

    @Override
    public MPA update(MPA o) {
        return null;
    }
}
