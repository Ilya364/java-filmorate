package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dal.impl.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext (classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FilmDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmDaoImpl dao;
    private UserDaoImpl userDao;

    @BeforeEach
    public void init() {
        dao = new FilmDaoImpl(jdbcTemplate);
        userDao = new UserDaoImpl(jdbcTemplate);
    }

    @Test
    public void testAddAndGetFilm() {
        MPA mpa = new MPA((short) 1, "G");
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2010, 10, 5))
                .duration(120)
                .rate(32)
                .mpa(mpa)
                .build();
        dao.add(film);
        
        Film saved = dao.get(1);
        
        assertThat(saved)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testRemoveFilm() {
        MPA mpa = new MPA((short) 1, "G");
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2010, 10, 5))
                .duration(120)
                .rate(32)
                .mpa(mpa)
                .build();
        dao.add(film);
        
        dao.remove(1);
        
        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> dao.get(1),
                "Ожидалось исключение NotFoundException"
        );
        
        assertTrue(e.getMessage().contains("Фильм не найден"));
    }

    @Test
    public void testUpdateFilm() {
        MPA mpa = new MPA((short) 1, "G");
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2010, 10, 5))
                .duration(120)
                .rate(32)
                .mpa(mpa)
                .build();
        dao.add(film);
        
        film = Film.builder()
                .id(1)
                .name("nameNew")
                .description("descNew")
                .releaseDate(LocalDate.of(2010, 10, 5))
                .duration(130)
                .rate(14)
                .mpa(mpa)
                .build();
        dao.update(film);
        
        assertEquals(film, dao.get(1));
    }

    @Test
    public void testGetAllFilms() {
        MPA mpa = new MPA((short) 1, "G");
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2010, 10, 5))
                .duration(120)
                .rate(32)
                .mpa(mpa)
                .build();
        MPA mpa2 = new MPA((short) 2, "PG");
        Film film2 = Film.builder()
                .name("name2")
                .description("desc2")
                .releaseDate(LocalDate.of(2012, 1, 5))
                .duration(100)
                .rate(32)
                .mpa(mpa2)
                .build();
        dao.add(film);
        dao.add(film2);
        
        assertIterableEquals(List.of(film, film2), dao.getAll());
    }

    @Test
    public void testAddLike() {
        MPA mpa = new MPA((short) 1, "G");
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2010, 10, 5))
                .duration(120)
                .rate(32)
                .mpa(mpa)
                .build();
        User user = User.builder()
                .name("name")
                .login("login")
                .email("usermail.03@yandex.ru")
                .birthday(LocalDate.of(2003, 6, 4))
                .build();
        dao.add(film);
        userDao.add(user);
        dao.addLike(1, 1);
        
        long[] ids = jdbcTemplate.query(
                "SELECT * FROM likes WHERE user_id = ? AND film_id = ?;",
                rs -> {
                    rs.next();
                    long userId = rs.getLong("user_id");
                    long filmId = rs.getLong("film_id");
                    return new long[] {userId, filmId};
                }, 1, 1);
        long[] expected = {1, 1};
        
        assertArrayEquals(expected, ids);
    }

    @Test
    public void testRemoveLike() {
        MPA mpa = new MPA((short) 1, "G");
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2010, 10, 5))
                .duration(120)
                .rate(32)
                .mpa(mpa)
                .build();
        User user = User.builder()
                .name("name")
                .login("login")
                .email("usermail.03@yandex.ru")
                .birthday(LocalDate.of(2003, 6, 4))
                .build();
        dao.add(film);
        userDao.add(user);
        dao.addLike(1, 1);
    
        dao.deleteLike(1, 1);
        
        Throwable e = assertThrows(
                Throwable.class,
                () -> jdbcTemplate.query(
                "SELECT * FROM likes WHERE user_id = 1 AND film_id = 1;",
                rs -> {
                    rs.next();
                    long userId = rs.getLong("user_id");
                    long filmId = rs.getLong("film_id");
                    return new long[] {userId, filmId};
                }
                )
        );
    }

    @Test
    public void testGetMostPopularFilms() {
        MPA mpa = new MPA((short) 1, "G");
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2010, 10, 5))
                .duration(120)
                .rate(10)
                .mpa(mpa)
                .build();
        MPA mpa2 = new MPA((short) 2, "PG");
        Film film2 = Film.builder()
                .name("name2")
                .description("desc2")
                .releaseDate(LocalDate.of(2012, 1, 5))
                .duration(100)
                .rate(32)
                .mpa(mpa2)
                .build();
        MPA mpa3 = new MPA((short) 3, "PG-13");
        Film film3 = Film.builder()
                .name("name3")
                .description("desc3")
                .releaseDate(LocalDate.of(2005, 10, 2))
                .duration(100)
                .rate(4)
                .mpa(mpa2)
                .build();
        dao.add(film);
        dao.add(film2);
        dao.add(film3);
        
        List<Film> popular = dao.getMostPopular(2);
        
        assertIterableEquals(List.of(film2, film), popular);
    }
}
