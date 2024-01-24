package ru.yandex.practicum.filmorate.storageTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryFilmStorageTest {
    private FilmStorage filmStorage;
    Film film1;
    Film film2;
    Film film3;

    @BeforeEach
    void setUp() {
        this.filmStorage = new InMemoryFilmStorage();

        this.film1 = new Film("name1", "description1", "2001-01-01", 111);
        this.film2 = new Film("name2", "description2", "2002-02-02", 222);
        this.film3 = new Film("name3", "description3", "2003-03-03", 333);

        filmStorage.createFilm(film1); // id = 1
        filmStorage.createFilm(film2); // id = 2
        filmStorage.createFilm(film3); // id = 3
    }

    @Test
    void getAllFilm() {
        assertEquals(3, filmStorage.getAllFilm().size());
    }

    @Test
    void createFilm() {
        assertEquals(film1, filmStorage.findFilmById(1));
        assertEquals(film2, filmStorage.findFilmById(2));
        assertEquals(film3, filmStorage.findFilmById(3));
    }

    @Test
    void updateFilm() {
        Film newFilm = new Film("nameNew", "descriptionNew", "2001-01-01", 111);
        newFilm.setId(1);

        filmStorage.updateFilm(newFilm);

        assertEquals(newFilm, filmStorage.findFilmById(1));
    }
}