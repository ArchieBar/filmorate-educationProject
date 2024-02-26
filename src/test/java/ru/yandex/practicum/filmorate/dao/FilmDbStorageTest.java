package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

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
        Film newFilm = new Film(1, "Film1", "Description1", "2023-01-01", 120, 7.8, new Mpa(1, "G"));
        Film updatedFilm = new Film(1, "Film1_updated", "Description1_updated", "2023-04-01", 130, 8.0, new Mpa(2, "PG"));
        FilmDbStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        filmStorage.createFilm(newFilm);

        Film savedFilm = filmStorage.updateFilm(updatedFilm);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(updatedFilm);
    }
}