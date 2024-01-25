package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage()));
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Test
    public void methodForGettingAllMoviesShouldReturnCorrectNumberOfMovies() throws ValidationException {
        Film film1 = new Film("name", "description", "2000-01-01", 100);
        Film film2 = new Film("name", "description", "2000-01-01", 100);
        filmController.createFilm(film1);
        filmController.createFilm(film2);

        assertEquals(2, filmController.getAllFilms().size());
    }

    @Test
    public void createdMovieMustMatchTheMovieInMemory() throws ValidationException {
        Film film1 = new Film("name", "description", "2000-01-01", 100);
        filmController.createFilm(film1);

        assertEquals(film1, filmController.getAllFilms().get(0));
    }

    @Test
    public void afterUpdatingTheMovieOnlyTheUpdatedMovieShouldBeStoredInMemory() throws ValidationException {
        Film film1 = new Film("name", "description", "2000-01-01", 100);
        filmController.createFilm(film1);

        assertEquals(film1, filmController.getAllFilms().get(0));
        assertEquals(1, filmController.getAllFilms().size());

        Film filmNow = new Film("nameNew", "description", "2000-01-01", 100);
        filmNow.setId(film1.getId());
        filmController.updateFilm(filmNow);

        assertEquals(filmNow, filmController.getAllFilms().get(0));
        assertEquals(1, filmController.getAllFilms().size());
    }

    @Test
    public void ifYouTryToCreateAMovieWithTheWrongNameAnExceptionShouldBeThrown() {
        Film film = new Film(" ", "description", "2000-01-01", 100);

        assertFalse(validator.validate(film).isEmpty());
    }

    @Test
    public void ifYouTryToCreateAMovieWithIncorrectDescriptionAnExceptionShouldBeThrown() throws ValidationException {
        Film filmDescription201Char = new Film("name",
                "ОписаниеРовно201СимволОписаниеРовно201СимволОписаниеРовно201Символ" +
                        "ОписаниеРовно201СимволОписаниеРовно201СимволОписаниеРовно201Символ" +
                        "ОписаниеРовно201СимволОписаниеРовно201СимволОписаниеРовно201СимволОпи",
                "2000-01-01", 100);
        Film filmDescriptionMore200Char = new Film("filmDescriptionMore200Char",
                "ОписаниеБольше200СимволовОписаниеБольше200СимволовОписаниеБольше200Символов" +
                        "ОписаниеБольше200СимволовОписаниеБольше200СимволовОписаниеБольше200Символов" +
                        "ОписаниеБольше200СимволовОписаниеБольше200СимволовОписаниеБольше200Символов",
                "2000-01-01", 100);
        Film filmDescription200Char = new Film("filmDescription200Char",
                "ОписаниеРовно200СимволовОписаниеРовно200СимволовОписаниеРовно200Символов" +
                        "ОписаниеРовно200СимволовОписаниеРовно200СимволовОписаниеРовно200Символов" +
                        "ОписаниеРовно200СимволовОписаниеРовно200СимволовОписание",
                "2000-01-01", 100);

        Film responseFilmDescription200Char = filmController.createFilm(filmDescription200Char);

        assertFalse(validator.validate(filmDescription201Char).isEmpty());
        assertFalse(validator.validate(filmDescriptionMore200Char).isEmpty());

        assertTrue(validator.validate(filmDescription200Char).isEmpty());
        assertEquals(filmDescription200Char, responseFilmDescription200Char);
    }

    @Test
    public void ifYouTryToCreateAMovieWithIncorrectDataReleaseAnExceptionShouldBeThrown() throws ValidationException {
        Film beforeEarliestReleaseDataFilm = new Film("beforeEarliestReleaseDataFilm",
                "Новое_Описание_1", "1850-01-01", 100);
        Film earliestReleaseDataFilm = new Film("earliestReleaseDataFilm",
                "Новое_Описание_1", LocalDate.of(1895, 12, 28).toString(), 100);
        Film afterEarliestReleaseDataFilm = new Film("afterEarliestReleaseDataFilm",
                "Новое_Описание_1", "2000-01-01", 100);

        final ValidationException exceptionBeforeEarliestReleaseDataFilm = assertThrows(ValidationException.class,
                () -> filmController.createFilm(beforeEarliestReleaseDataFilm));
        Film responseEarliestReleaseDataFilm =
                filmController.createFilm(earliestReleaseDataFilm);
        Film responseAfterEarliestReleaseDataFilm =
                filmController.createFilm(afterEarliestReleaseDataFilm);

        assertTrue(validator.validate(beforeEarliestReleaseDataFilm).isEmpty());
        assertTrue(validator.validate(earliestReleaseDataFilm).isEmpty());
        assertTrue(validator.validate(afterEarliestReleaseDataFilm).isEmpty());
        assertEquals("Дата релиза фильма не может быть раньше 1895-12-28",
                exceptionBeforeEarliestReleaseDataFilm.getMessage());
        assertEquals(earliestReleaseDataFilm, responseEarliestReleaseDataFilm);
        assertEquals(afterEarliestReleaseDataFilm, responseAfterEarliestReleaseDataFilm);
    }

    @Test
    public void ifYouTryToCreateAMovieWithNegativeDurationAnExceptionShouldBeThrown() throws ValidationException {
        Film filmNegativeDuration = new Film("filmNegativeDuration",
                "Описание_1", "2000-01-01", -100);
        Film filmZeroDuration = new Film("filmZeroDuration",
                "Описание_1", "2000-01-01", 0);
        Film filmNormalDuration = new Film("filmNormalDuration",
                "Описание_1", "2000-01-01", 100);

        Film responseFilmZeroDuration = filmController.createFilm(filmZeroDuration);
        Film responseFilmNormalDuration = filmController.createFilm(filmNormalDuration);

        assertFalse(validator.validate(filmNegativeDuration).isEmpty());
        assertEquals(filmZeroDuration, responseFilmZeroDuration);
        assertEquals(filmNormalDuration, responseFilmNormalDuration);
    }
}