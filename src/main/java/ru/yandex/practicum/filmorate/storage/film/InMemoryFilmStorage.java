package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private static final LocalDate EARLIEST_RELEASE_DATE =
            LocalDate.of(1895, 12, 28);

    private final Map<Integer, Film> films = new HashMap<>();

    private int id = 1;

    private int createId() {
        return id++;
    }

    @Override
    public Film findFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            throw new FilmNotFountException(MessageFormat.format("Фильм с id: {0} не найден", filmId));
        }
        return films.get(filmId);
    }

    private void validateReleaseDataFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            log.debug(
                    MessageFormat.format("Дата релиза фильма раньше {0}: {1}", EARLIEST_RELEASE_DATE, film));
            throw new ValidationException("Дата релиза фильма не может быть раньше " +
                            EARLIEST_RELEASE_DATE.format(DateTimeFormatter.ISO_DATE));
        }
    }

    @Override
    public List<Film> getAllFilm() {
        return List.copyOf(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Вызов POST /films: " + film);
        validateReleaseDataFilm(film);
        if (films.containsKey(film.getId())) {
            throw new ValidationException(MessageFormat.format("Фильм с id: {0} уже добавлен", film.getId()));
        }
        film.setId(createId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Вызов PUT /films: " + film);
        validateReleaseDataFilm(film);
        if (!films.containsKey(film.getId())) {
            throw new FilmNotFountException(
                    MessageFormat.format("Фильм с id: {0} не найден", film.getId()));
        }
        films.put(film.getId(), film);
        return film;
    }
}
