package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AllArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.UserDao;
import ru.yandex.practicum.filmorate.dal.mapper.UserMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
@AllArgsConstructor
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User add(User user) {
        String sql = "INSERT INTO users (name, birthday, login, email) " +
                          "VALUES (?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
                stmt.setString(1, user.getName());
                stmt.setDate(2, Date.valueOf(user.getBirthday()));
                stmt.setString(3, user.getLogin());
                stmt.setString(4, user.getEmail());
                return stmt;
                }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users " +
                "SET name = ?, login = ?, email = ?, birthday = ? " +
                "WHERE id = ?;";
        String sqlCheck = "SELECT * " +
                "FROM users " +
                "WHERE id = ?;";

        if (get(user.getId()) == null) {
            throw new NotFoundException("Пользователь не найден " + user.getId());
        }
        User updated;
        jdbcTemplate.update(sqlQuery,
                user.getName(), user.getLogin(), user.getEmail(), user.getBirthday(), user.getId());
        updated = jdbcTemplate.queryForObject(sqlCheck, UserMapper.userRowMapper(), user.getId());
        return updated;
    }

    @Override
    public User get(long id) {
        String sql = "SELECT * " +
                "FROM users u " +
                "WHERE u.id = ?;";
        try {
            return jdbcTemplate.queryForObject(sql, UserMapper.userRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь не найден");
        }
    }

    @Override
    public void remove(long id) {
        String sql = "DELETE FROM users " +
                "WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * " +
                "FROM users;";
        return jdbcTemplate.query(sql, UserMapper.userRowMapper());
    }

    @Override
    public List<User> getUserFriends(long id) {
        String sql = "SELECT u.id, u.name, u.login, u.email, u.birthday " +
                "FROM users u " +
                "JOIN friends f ON f.second_user_id = u.id " +
                "WHERE f.first_user_id = ?";
        return jdbcTemplate.query(sql, UserMapper.userRowMapper(), id);
    }

    @Override
    public void addToFriends(long userId, long friendId) {
        String sql = "INSERT INTO friends " +
                "VALUES (?, ?);";
        try {
            jdbcTemplate.update(sql, userId, friendId);
        } catch (RuntimeException e) {
            throw new NotFoundException("Одного из пользователей не существует");
        }
    }

    @Override
    public void removeFromFriends(long userId, long friendId) {
        String sqlQuery = "DELETE FROM friends " +
                "WHERE first_user_id = ? and second_user_id = ?;";
        try {
            jdbcTemplate.update(sqlQuery, userId, friendId);
        } catch (Throwable e) {
            throw new NotFoundException("Одного из пользователей не существует");
        }
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        String sqlQuery = "SELECT * " +
                "FROM users " +
                "WHERE id IN (SELECT f.second_user_id" +
                "                  FROM friends f" +
                "                  INNER JOIN (SELECT second_user_id " +
                "                              FROM friends" +
                "                              WHERE first_user_id = ?) o ON f.second_user_id = o.second_user_id" +
                "                  WHERE f.first_user_id = ?);";
        return jdbcTemplate.query(sqlQuery, UserMapper.userRowMapper(), userId, otherId);
    }
}
