package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface FilmStorage {
    Film findFilmById(Integer filmId) throws SQLException, IOException;

    List<Film> getAllFilm();

    Film createFilm(Film film) throws SQLException, IOException;

    Film updateFilm(Film film) throws SQLException, IOException;

    void saveLikes(Film film);
}
