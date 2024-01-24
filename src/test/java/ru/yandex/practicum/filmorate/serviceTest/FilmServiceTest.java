package ru.yandex.practicum.filmorate.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmServiceTest {
    private FilmStorage filmStorage;
    private FilmService filmService;

    @BeforeEach
    void setUp() {
        this.filmStorage = new InMemoryFilmStorage();
        this.filmService = new FilmService(filmStorage);

        Film film1 = new Film("name1", "description1", "2001-01-01", 111);
        Film film2 = new Film("name2", "description2", "2002-02-02", 222);
        Film film3 = new Film("name3", "description3", "2003-03-03", 333);

        filmStorage.createFilm(film1); // id = 1
        filmStorage.createFilm(film2); // id = 2
        filmStorage.createFilm(film3); // id = 3
    }

    @Test
    void addLike() {
        filmService.addLike(1, 1);
        filmService.addLike(2, 1);
        filmService.addLike(2, 2);
        filmService.addLike(3, 1);
        filmService.addLike(3, 2);
        filmService.addLike(3, 3);

        assertEquals(1, filmStorage.findFilmById(1).getCountLikes());
        assertEquals(2, filmStorage.findFilmById(2).getCountLikes());
        assertEquals(3, filmStorage.findFilmById(3).getCountLikes());
    }

    @Test
    void deleteLike() {
        filmService.addLike(1, 1);
        filmService.addLike(2, 1);
        filmService.addLike(2, 2);
        filmService.addLike(3, 1);
        filmService.addLike(3, 2);
        filmService.addLike(3, 3);

        filmService.deleteLike(2, 1);
        filmService.deleteLike(3, 1);
        filmService.deleteLike(3, 2);

        assertEquals(1, filmStorage.findFilmById(1).getCountLikes());
        assertEquals(1, filmStorage.findFilmById(2).getCountLikes());
        assertEquals(1, filmStorage.findFilmById(3).getCountLikes());
    }

    @Test
    void getPopularFilms() {
        filmService.addLike(1, 1);
        filmService.addLike(2, 1);
        filmService.addLike(2, 2);
        filmService.addLike(3, 1);
        filmService.addLike(3, 2);
        filmService.addLike(3, 3);

        List<Film> popFilmList = filmService.getPopularFilms(3);

        assertEquals(filmStorage.findFilmById(3), popFilmList.get(0));
        assertEquals(filmStorage.findFilmById(2), popFilmList.get(1));
        assertEquals(filmStorage.findFilmById(1), popFilmList.get(2));
    }
}