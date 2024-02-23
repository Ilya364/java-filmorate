package ru.yandex.practicum.filmorate.dal.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
@Qualifier("FilmDaoImpl")
@Slf4j
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;

    private Film buildFilm(ResultSet rs) throws SQLException {
        MPA mpa = new MPA(rs.getShort("rating_mpa_id"), rs.getString("rating_mpa"));
        return Film.builder()
                .id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rs.getInt("rate"))
                .mpa(mpa)
                .build();
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = buildFilm(rs);
            List<Genre> genres = new ArrayList<>();
            Genre genre;
            do {
                if (rs.getString("genre") != null) {
                    genre = new Genre(rs.getShort("genre_id"), rs.getString("genre"));
                    genres.add(genre);
                }
            } while (rs.next());
            film.addAllGenres(genres.stream().sorted(Comparator.comparing(Genre::getId)).collect(Collectors.toList()));
            return film;
        };
    }

    private List<Film> extractFilms(ResultSet rs) throws SQLException {
        List<Film> films = new ArrayList<>();
        boolean hasNext = rs.next();
        while (hasNext) {
            Film film = buildFilm(rs);
            List<Genre> genres = new ArrayList<>();
            do {
                if (rs.getString("genre") != null) {
                    genres.add(new Genre(rs.getShort("genre_id"), rs.getString("genre")));
                }
                hasNext = rs.next();
            } while (hasNext && rs.getLong("id") == film.getId());
            film.addAllGenres(genres.stream()
                            .sorted(Comparator.comparing(Genre::getId))
                            .collect(Collectors.toList()));
            films.add(film);
            genres.clear();
        }
        return films;
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

        String sqlAddGenre = "INSERT INTO film_genres (film_id, genre_id) " +
                "VALUES (?, ?);";
        for (Genre genre: film.getGenres()) {
            jdbcTemplate.update(sqlAddGenre, film.getId(), genre.getId());
        }
        return film;
    }

    @Override
    public Film get(long id) {
        String sqlGetFilm = "SELECT f.*, rm.name rating_mpa, g.id genre_id, g.name genre " +
                "FROM films f " +
                "LEFT JOIN rating_mpa rm on rm.id = f.rating_mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.id = fg.genre_id " +
                "WHERE f.id = ?;";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlGetFilm, filmRowMapper(), id);
        } catch (Throwable e) {
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
        String sqlAddGenre = "INSERT INTO film_genres (film_id, genre_id) " +
                             "VALUES (?, ?);";
        Film forUpdate;
        forUpdate = get(film.getId());
        if (forUpdate == null) {
            throw new NotFoundException("Фильм не найден " + film.getId());
        }

        jdbcTemplate.update(sqlUpdateFilm, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getRate(), film.getId());
        jdbcTemplate.update(sqlDeleteGenres, film.getId());
        for (Genre genre: film.getGenres()) {
            jdbcTemplate.update(sqlAddGenre, film.getId(), genre.getId());
        }

        return get(film.getId());
    }

    @Override
    public List<Film> getAll() {
        String sqlQuery = "SELECT f.*, rm.name rating_mpa, g.id genre_id, g.name genre " +
                "FROM films f " +
                "LEFT JOIN rating_mpa rm on rm.id = f.rating_mpa_id " +
                "LEFT JOIN film_genres fg ON fg.film_id = f.id " +
                "LEFT JOIN genres g ON g.id = fg.genre_id " +
                "ORDER BY f.id ASC;";
        return jdbcTemplate.query(sqlQuery, this::extractFilms);
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
                "ORDER BY f.rate DESC " +
                "LIMIT ?;";
        return jdbcTemplate.query(sqlQuery, this::extractFilms, count);
    }
}
