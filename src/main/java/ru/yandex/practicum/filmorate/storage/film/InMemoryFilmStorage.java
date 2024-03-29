package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("Film_In_Memory")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();

    private int id = 1;

    private int createId() {
        return id++;
    }

    @Override
    public Film findFilmById(Integer filmId) {
        if (!films.containsKey(filmId)) {
            log.debug(MessageFormat.format("Фильм с id: {0} не найден", filmId) +
                    "\n" + films);
            throw new FilmNotFountException(MessageFormat.format("Фильм с id: {0} не найден", filmId));
        }
        return films.get(filmId);
    }

    @Override
    public List<Film> getAllFilm() {
        return List.copyOf(films.values());
    }

    @Override
    public Film createFilm(Film film) {
        log.info("Вызов метода /createFilm - " + film);
        if (films.containsKey(film.getId())) {
            log.debug(MessageFormat.format("Фильм с id: {0} уже добавлен", film.getId()) +
                    "\n" + films);
            throw new ValidationException(MessageFormat.format("Фильм с id: {0} уже добавлен", film.getId()));
        }
        film.setId(createId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Вызов метода /updateFilm - " + film);
        findFilmById(film.getId());
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void saveLikes(Film film) {

    }
}
