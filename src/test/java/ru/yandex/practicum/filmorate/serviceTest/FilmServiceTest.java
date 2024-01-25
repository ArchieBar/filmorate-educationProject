package ru.yandex.practicum.filmorate.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilmServiceTest {
    private FilmService filmService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        UserStorage userStorage = new InMemoryUserStorage();
        this.filmService = new FilmService(new InMemoryFilmStorage(), userStorage);
        this.userService = new UserService(userStorage);

        Film film1 = new Film("name1", "description1", "2001-01-01", 111);
        Film film2 = new Film("name2", "description2", "2002-02-02", 222);
        Film film3 = new Film("name3", "description3", "2003-03-03", 333);

        User user1 = new User("example@gmail.com", "login1", "name1", "2001-01-01");
        User user2 = new User("example@gmail.com", "login2", "name2", "2002-02-03");
        User user3 = new User("example@gmail.com", "login3", "name3", "2002-03-03");

        filmService.createFilm(film1); // id = 1
        filmService.createFilm(film2); // id = 2
        filmService.createFilm(film3); // id = 3

        userService.createUser(user1); // id = 1
        userService.createUser(user2); // id = 2
        userService.createUser(user3); // id = 3
    }

    @Test
    void checkingForCorrectnessOfAddingLikes() {
        filmService.addLike(1, 1);
        filmService.addLike(2, 1);
        filmService.addLike(2, 2);
        filmService.addLike(3, 1);
        filmService.addLike(3, 2);
        filmService.addLike(3, 3);

        assertEquals(1, filmService.findFilmById(1).getCountLikes());
        assertEquals(2, filmService.findFilmById(2).getCountLikes());
        assertEquals(3, filmService.findFilmById(3).getCountLikes());
    }

    @Test
    void checkingForCorrectnessOfDeletingLikes() {
        filmService.addLike(1, 1);
        filmService.addLike(2, 1);
        filmService.addLike(2, 2);
        filmService.addLike(3, 1);
        filmService.addLike(3, 2);
        filmService.addLike(3, 3);

        filmService.deleteLike(2, 1);
        filmService.deleteLike(3, 1);
        filmService.deleteLike(3, 2);

        assertEquals(1, filmService.findFilmById(1).getCountLikes());
        assertEquals(1, filmService.findFilmById(2).getCountLikes());
        assertEquals(1, filmService.findFilmById(3).getCountLikes());
    }

    @Test
    void theCorrectListOfPopularMoviesShouldBeReturned() {
        filmService.addLike(1, 1);
        filmService.addLike(2, 1);
        filmService.addLike(2, 2);
        filmService.addLike(3, 1);
        filmService.addLike(3, 2);
        filmService.addLike(3, 3);

        List<Film> popFilmList = filmService.getPopularFilms(3);

        assertEquals(filmService.findFilmById(3), popFilmList.get(0));
        assertEquals(filmService.findFilmById(2), popFilmList.get(1));
        assertEquals(filmService.findFilmById(1), popFilmList.get(2));
    }

    //TODO
    // Добавить тесты на провал валидации
}