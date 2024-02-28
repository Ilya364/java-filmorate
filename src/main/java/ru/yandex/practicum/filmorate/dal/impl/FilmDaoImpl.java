package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.FilmDao;
import ru.yandex.practicum.filmorate.dal.mapper.FilmMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@AllArgsConstructor
@Slf4j
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    private void addFilmGenres(Film film) {
        String sqlAddGenre = "INSERT INTO film_genres (genre_id, film_id) " +
                "VALUES (%d, %d)";
        Set<Genre> genres = film.getGenres();
        int count = 1;
        for (Genre genre: genres) {
            sqlAddGenre = String.format(sqlAddGenre, genre.getId(), film.getId());
            if (count < genres.size()) {
                sqlAddGenre = sqlAddGenre + ", (%d, %d)";
            } else {
                sqlAddGenre = sqlAddGenre + ";";
            }
            count++;
        }
        if (!genres.isEmpty()) {
            jdbcTemplate.update(sqlAddGenre);
        }
    }

    @Override
    public Film add(Film film) {
        String sqlAddFilm = "INSERT INTO films (name, description, release_date, duration, rating_mpa_id, rate) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlAddFilm, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            stmt.setInt(6, film.getRate());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        addFilmGenres(film);

        return film;
    }

    @Override
    public Film get(long id) {
        String sqlGetFilm = "SELECT f.*, rm.name rating_mpa, g.id genre_id, g.name genre " +
                "FROM films f " +
                "LEFT JOIN rating_mpa rm on rm.id = f.rating_mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.id = fg.genre_id " +
                "WHERE f.id = ? " +
                "ORDER BY genre_id ASC;";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlGetFilm, FilmMapper.filmRowMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм не найден");
        }
        return film;
    }

    @Override
    public void remove(long id) {
        String sql = "DELETE FROM films " +
                "WHERE id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film update(Film film) {
        String sqlUpdateFilm = "UPDATE films " +
                          "SET name = ?, description = ?, release_date = ?, duration = ?, rating_mpa_id = ?, rate = ? " +
                          "WHERE id = ?;";
        String sqlDeleteGenres = "DELETE FROM film_genres " +
                                 "WHERE film_id = ?;";

        Film forUpdate = get(film.getId());
        if (forUpdate == null) {
            throw new NotFoundException("Фильм не найден " + film.getId());
        }

        jdbcTemplate.update(sqlUpdateFilm, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        jdbcTemplate.update(sqlDeleteGenres, film.getId());

        addFilmGenres(film);

        return get(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.*, rm.name rating_mpa, g.id genre_id, g.name genre " +
                "FROM films f " +
                "LEFT JOIN rating_mpa rm on rm.id = f.rating_mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.id = fg.genre_id " +
                "ORDER BY f.id ASC, genre_id ASC;";
        return jdbcTemplate.query(sqlQuery, FilmMapper::extractFilms);
    }

    @Override
    public void addLike(long userId, long filmId) {
        String sqlAddLike = "INSERT into likes " +
                "VALUES (?, ?);";
        jdbcTemplate.update(sqlAddLike, userId, filmId);
        log.info("Успешно добавлен лайк");
        String sqlIncRate = "UPDATE films " +
                "SET rate = rate + 1 " +
                "WHERE id = ?;";
        jdbcTemplate.update(sqlIncRate, filmId);
    }

    @Override
    public void deleteLike(long userId, long filmId) {
        String sqlRemoveLike = "DELETE FROM likes " +
                "WHERE user_id = ? and film_id = ?;";
        String sqlCheck = "SELECT * " +
                "FROM likes " +
                "WHERE user_id = ? and film_id = ?;";
        if (jdbcTemplate.queryForList(sqlCheck, userId, filmId).isEmpty()) {
            throw new NotFoundException("Такого лайка нет");
        }
        jdbcTemplate.update(sqlRemoveLike, userId, filmId);

        String sqlDecRate = "UPDATE films " +
                "SET rate = rate - 1 " +
                "WHERE id = ?;";
        jdbcTemplate.update(sqlDecRate, filmId);
    }

    @Override
    public List<Film> getMostPopular(long count) {
        String sqlQuery = "SELECT f.*, rm.name rating_mpa, g.id genre_id, g.name genre " +
                "FROM films f " +
                "LEFT JOIN rating_mpa rm on rm.id = f.rating_mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.id = fg.genre_id " +
                "ORDER BY f.rate DESC, genre_id ASC " +
                "LIMIT ?;";
        return jdbcTemplate.query(sqlQuery, FilmMapper::extractFilms, count);
    }
}
