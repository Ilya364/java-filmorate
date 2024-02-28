package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MPA;

public class MpaMapper {
    public static RowMapper<MPA> mpaRowMapper() {
        return (rs, rowNum) -> new MPA(rs.getShort("id"),
                (rs.getString("name")));
    }
}
