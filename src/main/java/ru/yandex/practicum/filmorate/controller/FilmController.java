package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable Integer filmId) {
        return filmService.findFilmById(filmId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilms() {
        return filmService.getAllFilm();
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        log.info("Вызов метода POST: /createFilm - " + film);
        return filmService.createFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Вызов метода PUT: /updateFilm - " + film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> addFilmLike(@PathVariable Map<String, String> pathId) {
        log.info("Вызов метода PUT: /addFilmLike - " + pathId);
        Integer filmId = Integer.parseInt(pathId.get("filmId"));
        Integer userid = Integer.parseInt(pathId.get("userId"));
        return filmService.addLike(filmId, userid);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> deleteLike(@PathVariable Map<String, String> pathId) {
        log.info("Вызов метода DELETE: /deleteLike - " + pathId);
        Integer filmId = Integer.parseInt(pathId.get("filmId"));
        Integer userid = Integer.parseInt(pathId.get("userId"));
        return filmService.deleteLike(filmId, userid);
    }
}
