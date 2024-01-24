package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    private final ObjectMapper mapper;

    public FilmController(ObjectMapper mapper) {
        this.mapper= mapper;
    }

    private int createId() {
        return id++;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        log.info("Вызов POST /films: " + film);
        validateReleaseDataFilm(film);
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id: " + " уже добавлен");
        }
        film.setId(createId());
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        log.info("Вызов PUT /films: " + film);
        validateReleaseDataFilm(film);
        if (!films.containsKey(film.getId())) {
            return new ResponseEntity<>(film, HttpStatus.NOT_FOUND);
        }
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    private void validateReleaseDataFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(earliestReleaseDate)) {
            log.debug("Дата релиза фильма раньше " + earliestReleaseDate +
                    ": " + film);
            throw new ValidationException("Дата релиза фильма не может быть раньше " + earliestReleaseDate.format(DateTimeFormatter.ISO_DATE));
        }
    }
}
