package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    void methodForGettingAllMoviesShouldReturnCorrectNumberOfMovies() throws ValidationException {
        Film film1 = new Film("name", "description", "2000-01-01", 100);
        Film film2 = new Film("name", "description", "2000-01-01", 100);
        filmController.createFilm(film1);
        filmController.createFilm(film2);

        assertEquals(2, filmController.getAllFilm().size());
    }

    @Test
    void createdMovieMustMatchTheMovieInMemory() throws ValidationException {
        Film film1 = new Film("name", "description", "2000-01-01", 100);
        filmController.createFilm(film1);

        assertEquals(film1, filmController.getAllFilm().get(0));
    }

    @Test
    void afterUpdatingTheMovieOnlyTheUpdatedMovieShouldBeStoredInMemory() throws ValidationException {
        Film film1 = new Film("name", "description", "2000-01-01", 100);
        filmController.createFilm(film1);

        assertEquals(film1, filmController.getAllFilm().get(0));
        assertEquals(1, filmController.getAllFilm().size());

        Film filmNow = new Film("nameNew", "description", "2000-01-01", 100);
        filmNow.setId(film1.getId());
        filmController.updateFilm(filmNow);

        assertEquals(filmNow, filmController.getAllFilm().get(0));
        assertEquals(1, filmController.getAllFilm().size());
    }

    @Test
    void ifYouTryToCreateAMovieWithTheWrongNameAnExceptionShouldBeThrown() {
        final ValidationException exceptionFilmEmptyName = assertThrows(ValidationException.class,
                () -> filmController.createFilm(new Film(" ", "description",
                        "2000-01-01", 100)));

        assertEquals("Название фильма не может быть пустым.", exceptionFilmEmptyName.getMessage());
    }

    @Test
    void ifYouTryToCreateAMovieWithIncorrectDescriptionAnExceptionShouldBeThrown() throws ValidationException {
        final ValidationException exceptionFilmDescription201Char = assertThrows(ValidationException.class,
                () -> filmController.createFilm(new Film("name",
                        "ОписаниеРовно201СимволОписаниеРовно201СимволОписаниеРовно201Символ" +
                                "ОписаниеРовно201СимволОписаниеРовно201СимволОписаниеРовно201Символ" +
                                "ОписаниеРовно201СимволОписаниеРовно201СимволОписаниеРовно201СимволОпи",
                        "2000-01-01", 100)));


        final ValidationException exceptionFilmDescriptionMore200Char = assertThrows(ValidationException.class,
                () -> filmController.createFilm(new Film("filmDescriptionMore200Char",
                        "ОписаниеБольше200СимволовОписаниеБольше200СимволовОписаниеБольше200Символов" +
                                "ОписаниеБольше200СимволовОписаниеБольше200СимволовОписаниеБольше200Символов" +
                                "ОписаниеБольше200СимволовОписаниеБольше200СимволовОписаниеБольше200Символов",
                        "2000-01-01", 100)));

        filmController.createFilm(new Film(
                "filmDescription200Char",
                "ОписаниеРовно200СимволовОписаниеРовно200СимволовОписаниеРовно200Символов" +
                        "ОписаниеРовно200СимволовОписаниеРовно200СимволовОписаниеРовно200Символов" +
                        "ОписаниеРовно200СимволовОписаниеРовно200СимволовОписание",
                "2000-01-01", 100));

        assertEquals(1, filmController.getAllFilm().size());
        assertEquals("Описание фильма не может быть больше 200 символов. " +
                "Символов: " + 201, exceptionFilmDescription201Char.getMessage());
        assertEquals("Описание фильма не может быть больше 200 символов. " +
                "Символов: " + 225, exceptionFilmDescriptionMore200Char.getMessage());
    }

    @Test
    void ifYouTryToCreateAMovieWithIncorrectDataReleaseAnExceptionShouldBeThrown() throws ValidationException {
        Film beforeEarliestReleaseDataFilm = new Film("beforeEarliestReleaseDataFilm", "Новое_Описание_1",
                "1850-01-01", 100);
        Film earliestReleaseDataFilm = new Film("earliestReleaseDataFilm", "Новое_Описание_1",
                LocalDate.of(1895, 12, 28).toString(), 100);
        Film afterEarliestReleaseDataFilm = new Film("afterEarliestReleaseDataFilm", "Новое_Описание_1",
                "2000-01-01", 100);

        final ValidationException beforeEarliestReleaseDataException = assertThrows(ValidationException.class,
                () -> filmController.createFilm(beforeEarliestReleaseDataFilm));
        filmController.createFilm(earliestReleaseDataFilm);
        filmController.createFilm(afterEarliestReleaseDataFilm);

        assertEquals("Дата релиза фильма не может быть раньше " +
                        LocalDate.of(1895, 12, 28).format(DateTimeFormatter.ISO_DATE),
                beforeEarliestReleaseDataException.getMessage());
        assertEquals(2, filmController.getAllFilm().size());
    }

    @Test
    void ifYouTryToCreateAMovieWithNegativeDurationAnExceptionShouldBeThrown() throws ValidationException {
        Film filmNegativeDuration = new Film("filmNegativeDuration", "Описание_1",
                "2000-01-01", -100);
        Film filmZeroDuration = new Film("filmZeroDuration", "Описание_1",
                "2000-01-01", 0);
        Film filmNormalDuration = new Film("filmNormalDuration", "Описание_1",
                "2000-01-01", 100);

        final ValidationException exceptionNegativeDuration = assertThrows(ValidationException.class,
                () -> filmController.createFilm(filmNegativeDuration));
        filmController.createFilm(filmZeroDuration);
        filmController.createFilm(filmNormalDuration);

        assertEquals("Продолжительность фильма должна быть положительной. " +
                "Продолжительность: " + filmNegativeDuration.getDuration(), exceptionNegativeDuration.getMessage());
        assertEquals(2, filmController.getAllFilm().size());
    }
}