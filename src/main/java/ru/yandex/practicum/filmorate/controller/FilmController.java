package ru.yandex.practicum.filmorate.controller;

import com.google.gson.Gson;
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
    private final Gson gson = new Gson();
    private final LocalDate earliestReleaseDate =
            LocalDate.of(1895, 12, 28);
    private int id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    private int createId() {
        return id++;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return List.copyOf(films.values());
    }

    @PostMapping
    public ResponseEntity<Object> createFilm(@Valid @RequestBody Film film) {
        log.info("Вызов POST /films: " + film);
        validateReleaseDataFilm(film);
        film.setId(createId());
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Object> updateFilm(@Valid @RequestBody Film film) {
        log.info("Вызов PUT /films: " + film);
        validateReleaseDataFilm(film);
        if (!films.containsKey(film.getId())) {
            return new ResponseEntity<>(gson.toJson("Фильм c id: " + film.getId() + " не найден"),
                    HttpStatus.NOT_FOUND);
        }
        films.put(film.getId(), film);
        return new ResponseEntity<>(film, HttpStatus.OK);
    }

    @ExceptionHandler(ValidationException.class)
    private ResponseEntity<String> handleValidationException(ValidationException exception) {
        return new ResponseEntity<>(gson.toJson("Ошибка валидации: " + exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return new ResponseEntity<>(gson.toJson("Ошибка валидации: " + exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    private void validateReleaseDataFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(earliestReleaseDate)) {
            log.debug("Дата релиза фильма раньше " + earliestReleaseDate +
                    ": " + film);
            throw new ValidationException("Дата релиза фильма не может быть раньше " + earliestReleaseDate.format(DateTimeFormatter.ISO_DATE));
        }
    }
}
