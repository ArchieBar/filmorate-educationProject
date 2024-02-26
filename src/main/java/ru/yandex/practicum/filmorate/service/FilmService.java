package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFountException;
import ru.yandex.practicum.filmorate.exception.LikeCreatePreviouslyException;
import ru.yandex.practicum.filmorate.exception.UserNotFountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.io.IOException;
import java.sql.SQLException;
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
    private final FilmStorage filmStorage; // @Primary in FilmDbStorage (Film_DAO)
    private final UserStorage userStorage; // @Primary in UserDbStorage (User_DAO)

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

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
        return getAllFilm().stream()
                .sorted(Comparator.comparingInt(Film::getCountLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(Integer filmId) throws SQLException, IOException {
        Film film = filmStorage.findFilmById(filmId);
        if (film != null) {
            return film;
        } else {
            throw new FilmNotFountException(MessageFormat.format("Фильм с id: {0} не найден", film));
        }
    }

    public List<Film> getAllFilm() {
        return filmStorage.getAllFilm();
    }

    public List<Integer> addLike(Integer filmId, Integer userId) throws SQLException, IOException {
        log.info(MessageFormat.format("Вызов метода: /addLike - filmId: {0}, userId: {1}", filmId, userId));
        Film film = filmStorage.findFilmById(filmId);
        if (film.getLikes().contains(userId)) {
            throw new LikeCreatePreviouslyException(
                    MessageFormat.format("Пользователь с id: {0} уже поставил лайк", userId));
        }
        film.setLike(userStorage.findUserById(userId).getId());
        filmStorage.saveLikes(film);
        return new ArrayList<>(film.getLikes());
    }

    public Film createFilm(Film film) throws SQLException, IOException {
        log.info("Вызов метода: /createFilm - " + film);
        film = filmStorage.createFilm(validateReleaseDataFilm(film));
        return film;
    }

    public Film updateFilm(Film film) throws SQLException, IOException {
        log.info("Вызов метода: /updateFilm - " + film);
        film = filmStorage.updateFilm(film);
        return film;
    }

    public List<Integer> deleteLike(Integer filmId, Integer userId) throws SQLException, IOException {
        log.info(MessageFormat.format("Вызов метода: /deleteLike - filmId: {0}, userId: {1}", filmId, userId));
        Film film = filmStorage.findFilmById(filmId);
        log.info(film.toString());
        if (!film.getLikes().contains(userId)) {
            throw new UserNotFountException(
                    MessageFormat.format("Лайк пользователя с id: {0} не найден", userId));
        }
        film.removeLike(userId);
        filmStorage.saveLikes(film);
        return new ArrayList<>(film.getLikes());
    }
}
