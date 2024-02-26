package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.film.Film;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface FilmStorage {
    public Film findFilmById(Integer filmId) throws SQLException, IOException;

    public List<Film> getAllFilm();

    public Film createFilm(Film film) throws SQLException, IOException;

    public Film updateFilm(Film film) throws SQLException, IOException;

    public void saveLikes(Film film);
}
