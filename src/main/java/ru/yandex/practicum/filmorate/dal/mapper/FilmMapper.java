package ru.yandex.practicum.filmorate.dal.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FilmMapper {
    private static Film buildFilm(ResultSet rs) throws SQLException {
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

    public static RowMapper<Film> filmRowMapper() {
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
            film.addAllGenres(genres);
            return film;
        };
    }

    public static List<Film> extractFilms(ResultSet rs) throws SQLException {
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
}
