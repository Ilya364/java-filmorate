package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

public class UserMapper {
    public static RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> User.builder()
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .id(rs.getInt("id"))
                .build();
    }
}
