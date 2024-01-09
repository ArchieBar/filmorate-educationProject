package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<String, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilm() {
        return List.copyOf(films.values());
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@RequestBody Film film) throws ValidationException {
        log.info("Вызов POST /films");
        try {
            checkValidityFilm(film);
        } catch (ValidationException exception) {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
        films.put(film.getName(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@RequestBody Film film) throws ValidationException {
        log.info("Вызов PUT /films");
        try {
            checkValidityFilm(film);
        } catch (ValidationException exception) {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
        films.put(film.getName(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    private void checkValidityFilm(Film film) throws ValidationException {
        if (film.getName() == null || film.getName().isBlank()) {
            log.debug("Название фильма пустое: " + film);
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            log.debug("Описание фильма больше 200 символов. Количество символов: " + film.getDescription().length() +
                    "Фильм: " + film);
            throw new ValidationException("Описание фильма не может быть больше 200 символов. " +
                    "Символов: " + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(Film.EARLIEST_RELEASE_DATE)) {
            log.debug("Дата релиза фильма раньше " + Film.EARLIEST_RELEASE_DATE +
                    ": " + film);
            throw  new ValidationException("Дата релиза фильма не может быть раньше " + Film.EARLIEST_RELEASE_DATE.format(DateTimeFormatter.ISO_DATE));
        }
        if (film.getDuration().isNegative()) {
            log.debug("Продолжительность фильма отрицательная: " + film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной. " +
                    "Продолжительность: " + film.getDuration());
        }
    }
}
