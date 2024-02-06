package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    public Film findFilmById(Integer filmId);

    public List<Film> getAllFilm();

    public Film createFilm(Film film);

    public Film updateFilm(Film film);
}
