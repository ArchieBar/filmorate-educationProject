package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.user.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindFilmById() throws SQLException, IOException {
        Film film = new Film(10, "Film10", "Description10", "2023-03-01", 140, 8.5, new Mpa(3, "PG-13"));
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        filmDbStorage.createFilm(film);
        Film findFilm = filmDbStorage.findFilmById(film.getId());

        assertThat(findFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testCreateFilm() throws SQLException, IOException {
        Film newFilm = new Film(3, "Film3", "Description3", "2023-03-01", 140, 8.5, new Mpa(3, "PG-13"));
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);

        Film savedFilm = filmStorage.createFilm(newFilm);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newFilm);
    }

    @Test
    public void testUpdateFilm() throws SQLException, IOException {
        Film newFilm = new Film(2, "Film1", "Description1", "2023-01-01", 120, 7.8, new Mpa(1, "G"));
        Film updatedFilm = new Film(2, "Film1_updated", "Description1_updated", "2023-04-01", 130, 8.0, new Mpa(2, "PG"));
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.createFilm(newFilm);

        Film savedFilm = filmStorage.updateFilm(updatedFilm);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedFilm);
    }

    @Test
    public void testGetAllFilm() throws SQLException, IOException {
        Film film1 = new Film(101, "Film101", "Description101", "2023-01-01", 120, 7.8, new Mpa(1, "G"));
        Film film2 = new Film(102, "Film102", "Description102", "2023-01-01", 120, 7.8, new Mpa(1, "G"));
        Film film3 = new Film(103, "Film103", "Description103", "2023-01-01", 120, 7.8, new Mpa(1, "G"));
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        List<Film> films = new ArrayList<>();
        films.add(filmDbStorage.createFilm(film1));
        films.add(filmDbStorage.createFilm(film2));
        films.add(filmDbStorage.createFilm(film3));
        List<Film> findFilms = filmDbStorage.getAllFilm();

        assertThat(findFilms)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(films);
    }

    @Test
    public void testAddLike() throws SQLException, IOException {
        Film film = new Film(111, "Film111", "Description111", "2023-01-01", 120, 7.8, new Mpa(1, "G"));
        User user = new User(1, "email@email.ru", "name", "login", "2000-02-01");
        UserDbStorage userDbStorage = new UserDbStorage(jdbcTemplate);
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        filmDbStorage.createFilm(film);
        userDbStorage.createUser(user);

        film.setLike(user.getId());
        filmDbStorage.saveLikes(film);
        Film findFilm = filmDbStorage.findFilmById(film.getId());

        assertThat(findFilm.getCountLikes())
                .usingRecursiveComparison()
                .isEqualTo(film.getCountLikes());
    }

    @Test
    public void testSaveGenre() throws SQLException, IOException {
        Film film = new Film(141, "Film141", "Description141", "2023-01-01", 120, 7.8, new Mpa(1, "G"));
        FilmDbStorage filmDbStorage = new FilmDbStorage(jdbcTemplate);

        filmDbStorage.createFilm(film);
        Set<Genre> genreSet = new HashSet<>();
        genreSet.add(new Genre(1, "Комедия"));
        film.addGenre(new Genre(1, "Комедия"));
        filmDbStorage.saveGenre(film);
        Set<Genre> findGenres = filmDbStorage.findFilmById(film.getId()).getGenres();

        assertThat(findGenres)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genreSet);
    }
}