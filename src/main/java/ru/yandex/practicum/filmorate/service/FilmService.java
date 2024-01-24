package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFountException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    //TODO
    // Наверное, стоит добавить проверку на существование пользователя
    public List<Integer> addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId);
        film.setLike(userId);
        return new ArrayList<>(film.getLikes());
    }

    public List<Integer> deleteLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new UserNotFountException(
                    MessageFormat.format("Лайк пользователя с id: {0} не найден", userId));
        }
        film.removeLike(userId);
        return new ArrayList<>(film.getLikes());
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getAllFilm().stream()
                .sorted(Comparator.comparingInt(Film::getCountLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}
