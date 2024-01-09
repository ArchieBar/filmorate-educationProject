package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;
    @BeforeEach
    public void setUp() {
        filmController = new FilmController();
    }

    @Test
    void getAllFilm() throws ValidationException {
        Film film1 = new Film(1, "Фильм_1", "Описание_1",
                LocalDateTime.of(1900, 1, 1 , 0, 0), Duration.ofHours(1));
        Film film2 = new Film(2, "Фильм_2", "Описание_2",
                LocalDateTime.of(1900, 1, 1 , 0, 0), Duration.ofHours(1));
        filmController.createFilm(film1);
        filmController.createFilm(film2);

        assertEquals(2, filmController.getAllFilm().size());
    }

    @Test
    void createFilm() {
    }

    @Test
    void updateFilm() {
    }
}