package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    //TODO
    // Лучше ли сделать собственную аннотацию?
    // Егор говорит, что это плоха практика, но особо в этот вопрос я не углублялся
    private Film validateReleaseDataFilm(Film film) throws ValidationException {
        final LocalDate EARLIEST_RELEASE_DATE =
                LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            log.debug(
                    MessageFormat.format("Дата релиза фильма раньше {0}: {1}", EARLIEST_RELEASE_DATE, film));
            throw new ValidationException("Дата релиза фильма не может быть раньше " +
                    EARLIEST_RELEASE_DATE.format(DateTimeFormatter.ISO_DATE));
        }
        return film;
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getAllFilm().stream()
                .sorted(Comparator.comparingInt(Film::getCountLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(Integer filmId) {
        return filmStorage.findFilmById(filmId);
    }

    public List<Film> getAllFilm() {
        return filmStorage.getAllFilm();
    }

    public List<Integer> addLike(Integer filmId, Integer userId) {
        log.info(MessageFormat.format("Вызов метода: /addLike - filmId: {0}, userId: {1}", filmId, userId));
        Film film = filmStorage.findFilmById(filmId);
        film.setLike(userStorage.findUserByID(userId).getId_user());
        return new ArrayList<>(film.getLikes());
    }

    public Film createFilm(Film film) {
        log.info("Вызов метода: /createFilm - " + film);
        return filmStorage.createFilm(validateReleaseDataFilm(film));
    }

    public Film updateFilm(Film film) {
        log.info("Вызов метода: /updateFilm - " + film);
        return filmStorage.updateFilm(validateReleaseDataFilm(film));
    }

    public List<Integer> deleteLike(Integer filmId, Integer userId) {
        log.info(MessageFormat.format("Вызов метода: /deleteLike - filmId: {0}, userId: {1}", filmId, userId));
        Film film = filmStorage.findFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new UserNotFountException(
                    MessageFormat.format("Лайк пользователя с id: {0} не найден", userId));
        }
        film.removeLike(userId);
        return new ArrayList<>(film.getLikes());
    }
}
