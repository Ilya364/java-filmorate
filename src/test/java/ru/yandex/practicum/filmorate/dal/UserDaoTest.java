package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.dal.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private UserDaoImpl dao;

    @BeforeEach
    public void init() {
        dao = new UserDaoImpl(jdbcTemplate);
    }

    @Test
    public void testAddAndGetUser() {
        User user = User.builder()
                .name("name")
                .login("login")
                .email("usermail.03@yandex.ru")
                .birthday(LocalDate.of(2003, 6, 4))
                .build();
        dao.add(user);

        User saved = dao.get(1);

        assertThat(saved)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(user);
    }

    @Test
    public void testUpdateUser() {
        User user = User.builder()
                .name("name")
                .login("login")
                .email("usermail.03@yandex.ru")
                .birthday(LocalDate.of(2003, 6, 4))
                .build();
        dao.add(user);

        User updated = User.builder()
                .id(1)
                .name("updatedName")
                .login("updatedLogin")
                .email("updatedmail.01@yandex.ru")
                .birthday(LocalDate.of(2001, 6, 4))
                .build();
        dao.update(updated);

        assertEquals(updated, dao.get(1));
    }

    @Test
    public void testRemoveUser() {
        User user = User.builder()
                .name("name")
                .birthday(LocalDate.of(2010, 10, 5))
                .login("login")
                .email("usermail.03@yandex.ru")
                .build();
        dao.add(user);

        dao.remove(1);

        NotFoundException e = assertThrows(
                NotFoundException.class,
                () -> dao.get(1),
                "Ожидалось исключение NotFoundException"
        );

        assertTrue(e.getMessage().contains("Пользователь не найден"));
    }

    @Test
    public void testGetAllUsers() {
        User user = User.builder()
                .name("name")
                .birthday(LocalDate.of(2010, 10, 5))
                .login("login")
                .email("usermail.03@yandex.ru")
                .build();
        User user2 = User.builder()
                .name("name2")
                .birthday(LocalDate.of(2000, 10, 5))
                .login("login2")
                .email("usermail2.03@yandex.ru")
                .build();
        dao.add(user);
        dao.add(user2);

        assertIterableEquals(List.of(user, user2), dao.getAll());
    }

    @Test
    public void testAddAndGetUserFriends() {
        User user = User.builder()
                .name("name")
                .birthday(LocalDate.of(2010, 10, 5))
                .login("login")
                .email("usermail.03@yandex.ru")
                .build();
        User user2 = User.builder()
                .name("name2")
                .birthday(LocalDate.of(2000, 10, 5))
                .login("login2")
                .email("usermail2.03@yandex.ru")
                .build();
        dao.add(user);
        dao.add(user2);
        dao.addToFriends(1, 2);

        assertIterableEquals(List.of(user2), dao.getUserFriends(1));
    }

    @Test
    public void testRemoveFriend() {
        User user = User.builder()
                .name("name")
                .birthday(LocalDate.of(2010, 10, 5))
                .login("login")
                .email("usermail.03@yandex.ru")
                .build();
        User user2 = User.builder()
                .name("name2")
                .birthday(LocalDate.of(2000, 10, 5))
                .login("login2")
                .email("usermail2.03@yandex.ru")
                .build();
        dao.add(user);
        dao.add(user2);
        dao.addToFriends(1, 2);
        dao.removeFromFriends(1, 2);

        assertIterableEquals(List.of(), dao.getUserFriends(1));
    }

    @Test
    public void testGetCommonFriends() {
        User user = User.builder()
                .name("name")
                .birthday(LocalDate.of(2010, 10, 5))
                .login("login")
                .email("usermail.03@yandex.ru")
                .build();
        User user2 = User.builder()
                .name("name2")
                .birthday(LocalDate.of(2000, 10, 5))
                .login("login2")
                .email("usermail2.03@yandex.ru")
                .build();
        User user3 = User.builder()
                .name("name3")
                .birthday(LocalDate.of(2004, 10, 5))
                .login("login3")
                .email("usermail.04@yandex.ru")
                .build();
        dao.add(user);
        dao.add(user2);
        dao.add(user3);
        dao.addToFriends(1, 2);
        dao.addToFriends(3, 2);

        assertIterableEquals(List.of(user2), dao.getCommonFriends(1, 3));
    }
}
