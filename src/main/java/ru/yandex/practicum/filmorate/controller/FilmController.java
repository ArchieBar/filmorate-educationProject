package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage filmStorage;
    private final FilmService filmService;

    //FIXME
    // С этим есть небольшие вопросы
    @Autowired
    public FilmController(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
        this.filmService = new FilmService(filmStorage);
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable Integer filmId) {
        return filmStorage.findFilmById(filmId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilm();
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getPopularFilms(count);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmStorage.createFilm(film);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> addFilmLike(@PathVariable Map<String, String> pathId) {
        Integer filmId = Integer.parseInt(pathId.get("filmId"));
        Integer userid = Integer.parseInt(pathId.get("userId"));
        return filmService.addLike(filmId, userid);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Integer> deleteLike(@PathVariable Map<String, String> pathId) {
        Integer filmId = Integer.parseInt(pathId.get("filmId"));
        Integer userid = Integer.parseInt(pathId.get("userId"));
        return filmService.deleteLike(filmId, userid);
    }
}
