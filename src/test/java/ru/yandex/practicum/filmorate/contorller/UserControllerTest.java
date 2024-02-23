package ru.yandex.practicum.filmorate.contorller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateUser() {
        User user = User.builder()
                .name("name")
                .login("login")
                .email("ilya.trishkin.03@mail.ru")
                .birthday(LocalDate.of(2003, 4, 6))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(0, violations.size());
    }

    @Test
    public void shouldCreateUserWhenEmptyName() {
        User user = User.builder()
                .login("login")
                .email("ilya.trishkin.03@mail.ru")
                .birthday(LocalDate.of(2003, 4, 6))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(0, violations.size());
    }

    @Test
    public void shouldNotCreateUserWhenInvalidEmail() {
        User user = User.builder()
                .name("name")
                .login("login")
                .email("ilya.trishkinmail")
                .birthday(LocalDate.of(2003, 4, 6))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotCreateUserWhenEmptyLogin() {
        User user = User.builder()
                .name("name")
                .login("")
                .email("ilya.trishkin.03@mail.ru")
                .birthday(LocalDate.of(2003, 4, 6))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotCreateUserWhenFutureBirthday() {
        User user = User.builder()
                .name("name")
                .login("login")
                .email("ilya.trishkin.03@mail.ru")
                .birthday(LocalDate.of(2024, 4, 6))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(1, violations.size());
    }
}