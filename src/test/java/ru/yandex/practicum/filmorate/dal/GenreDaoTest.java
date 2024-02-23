package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.impl.GenreDaoImpl;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private GenreDaoImpl dao;

    @BeforeEach
    public void init() {
        dao = new GenreDaoImpl(jdbcTemplate);
    }

    @Test
    public void testGetGenre() {
        Genre genre = dao.get(1);
        
        assertEquals(new Genre((short) 1, "Комедия"), genre);
    }

    @Test
    public void testGetAllGenres() {
        List<Genre> genres = List.of(
                new Genre((short) 1, "Комедия"),
                new Genre((short) 2, "Драма"),
                new Genre((short) 3, "Мультфильм"),
                new Genre((short) 4, "Триллер"),
                new Genre((short) 5, "Документальный"),
                new Genre((short) 6, "Боевик")
        );
        List<Genre> dbGenres = dao.getAll();
        
        assertIterableEquals(genres, dbGenres);
    }
}
