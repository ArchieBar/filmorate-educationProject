package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final LocalDate earliestReleaseDate =
            LocalDate.of(1895, 12, 28);
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    private int createId() {
        return id++;
    }

    @GetMapping
    public List<Film> getAllFilm() {
        return List.copyOf(films.values());
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Вызов POST /films");
        if (checkValidityFilm(film)) {
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
        film.setId(createId());
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        log.info("Вызов PUT /films");
        if (checkValidityFilm(film)) {
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
        if (!films.containsKey(film.getId())) {
            return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
        }
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    private boolean checkValidityFilm(Film film) throws ValidationException {
        boolean flag = false;
        if (film.getName() == null || film.getName().isBlank()) {
            flag = true;
            log.debug("Название фильма пустое: " + film);
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            flag = true;
            log.debug("Описание фильма больше 200 символов. Количество символов: " + film.getDescription().length() +
                    "Фильм: " + film);
            throw new ValidationException("Описание фильма не может быть больше 200 символов. " +
                    "Символов: " + film.getDescription().length());
        }
        if (film.getReleaseDate().isBefore(earliestReleaseDate)) {
            flag = true;
            log.debug("Дата релиза фильма раньше " + earliestReleaseDate +
                    ": " + film);
            throw new ValidationException("Дата релиза фильма не может быть раньше " + earliestReleaseDate.format(DateTimeFormatter.ISO_DATE));
        }
        if (film.getDuration() < 0) {
            flag = true;
            log.debug("Продолжительность фильма отрицательная: " + film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной. " +
                    "Продолжительность: " + film.getDuration());
        }
        return flag;
    }
}
