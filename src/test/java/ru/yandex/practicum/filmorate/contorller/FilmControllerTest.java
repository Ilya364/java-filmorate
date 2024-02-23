package ru.yandex.practicum.filmorate.contorller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void shouldCreateFilm() {
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2020, 11, 15))
                .duration(50)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(0, violations.size());
    }

    @Test
    public void shouldNotCreateFilmWhenInvalidDate() {
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(1884, 11, 15))
                .duration(50)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotCreateFilmWhenEmptyName() {
        Film film = Film.builder()
                .name("")
                .description("desc")
                .releaseDate(LocalDate.of(2020, 11, 15))
                .duration(50)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotCreateFilmWhenDescriptionLonger200() {
        Film film = Film.builder()
                .name("name")
                .description("Кобб – талантливый вор, лучший из лучших в опасном искусстве извлечения: " +
                        "он крадет ценные секреты из глубин подсознания во время сна, когда человеческий " +
                        "разум наиболее уязвим. Редкие способности Кобба сделали его ценным игроком в привычном " +
                        "к предательству мире промышленного шпионажа, но они же превратили его в извечного " +
                        "беглеца и лишили всего, что он когда-либо любил.")
                .releaseDate(LocalDate.of(2020, 11, 15))
                .duration(50)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
    }

    @Test
    public void shouldNotCreateFilmWhenNegativeDuration() {
        Film film = Film.builder()
                .name("name")
                .description("desc")
                .releaseDate(LocalDate.of(2020, 11, 15))
                .duration(-50)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(1, violations.size());
    }
}