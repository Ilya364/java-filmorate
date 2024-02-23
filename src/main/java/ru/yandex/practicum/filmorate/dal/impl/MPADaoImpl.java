package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.MPADao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;
import java.util.List;

@Component
@AllArgsConstructor
public class MPADaoImpl implements MPADao {
    private final JdbcTemplate template;

    private RowMapper<MPA> mpaRowMapper() {
        return (rs, rowNum) -> new MPA(rs.getShort("id"),
                (rs.getString("name")));
    }

    @Override
    public MPA get(long id) {
        String sqlQuery = "SELECT * " +
                "FROM rating_mpa " +
                "WHERE id = ?;";
        try {
            return template.queryForObject(sqlQuery, mpaRowMapper(), id);
        } catch (Throwable e) {
            throw new NotFoundException("Рейтинга с таким id не существует.");
        }
    }

    @Override
    public List<MPA> getAll() {
        String sqlQuery = "SELECT * FROM rating_mpa " +
                "ORDER BY id ASC;";
        return template.query(sqlQuery, mpaRowMapper());
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
