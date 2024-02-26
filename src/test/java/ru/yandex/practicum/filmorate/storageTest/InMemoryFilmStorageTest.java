//package ru.yandex.practicum.filmorate.storageTest;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import ru.yandex.practicum.filmorate.model.film.Film;
//import ru.yandex.practicum.filmorate.model.film.Mpa;
//import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
//import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
//
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.HashSet;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class InMemoryFilmStorageTest {
//    private FilmStorage filmStorage;
//    Film film1;
//    Film film2;
//    Film film3;
//
//    @BeforeEach
//    void setUp() throws SQLException, IOException {
//        this.filmStorage = new InMemoryFilmStorage();
//
//        this.film1 = new Film("name1", "description1", "2001-01-01", 111, 1,
//                new Mpa(1), new HashSet<>());
//        this.film2 = new Film("name2", "description2", "2002-02-02", 222, 1,
//                new Mpa(1), new HashSet<>());
//        this.film3 = new Film("name3", "description3", "2003-03-03", 333, 1,
//                new Mpa(1), new HashSet<>());
//
//        filmStorage.createFilm(film1); // id = 1
//        filmStorage.createFilm(film2); // id = 2
//        filmStorage.createFilm(film3); // id = 3
//    }
//
//    @Test
//    void theCorrectListOfMoviesShouldBeReturned() {
//        assertEquals(3, filmStorage.getAllFilm().size());
//    }
//
//    @Test
//    void theFilmMustBeCreatedCorrectly() throws SQLException, IOException {
//        assertEquals(film1, filmStorage.findFilmById(1));
//        assertEquals(film2, filmStorage.findFilmById(2));
//        assertEquals(film3, filmStorage.findFilmById(3));
//    }
//
//    @Test
//    void theFilmMustBeUpdatingCorrectly() throws SQLException, IOException {
//        Film newFilm = new Film("nameNew", "descriptionNew", "2001-01-01", 111, 1,
//                new Mpa(1), new HashSet<>());
//        newFilm.setId(1);
//
//        filmStorage.updateFilm(newFilm);
//
//        assertEquals(newFilm, filmStorage.findFilmById(1));
//    }
//
//    //TODO
//    // Добавить тесты на провал валидации
//}