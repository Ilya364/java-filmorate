package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dal.impl.MPADaoImpl;
import ru.yandex.practicum.filmorate.model.MPA;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MPADaoTest {
    private final JdbcTemplate jdbcTemplate;
    private MPADaoImpl dao;

    @BeforeEach
    public void init() {
        dao = new MPADaoImpl(jdbcTemplate);
    }

    @Test
    public void testGetMpa() {
        MPA mpa = dao.get(1);
        
        assertEquals(new MPA((short) 1, "G"), mpa);
    }

    @Test
    public void testGetAllMpa() {
        List<MPA> mpaList = List.of(
                new MPA((short) 1, "G"),
                new MPA((short) 2, "PG"),
                new MPA((short) 3, "PG-13"),
                new MPA((short) 4, "R"),
                new MPA((short) 5, "NC-17")
        );
        List<MPA> dbMpaList = dao.getAll();
        
        assertIterableEquals(mpaList, dbMpaList);
    }
}
