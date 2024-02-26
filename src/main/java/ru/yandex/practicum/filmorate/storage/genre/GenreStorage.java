package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.film.Genre;

import java.util.List;

public interface GenreStorage {
    public List<Genre> getAllGenres();

    public Genre getGenreById(Integer id);
}
