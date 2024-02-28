package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;

public class GenreMapper {
    public static RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getShort("id"),
                (rs.getString("name")));
    }
}
