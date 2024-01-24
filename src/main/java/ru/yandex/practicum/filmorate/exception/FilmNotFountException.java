package ru.yandex.practicum.filmorate.exception;

public class FilmNotFountException extends RuntimeException {
    public FilmNotFountException(String message) {
        super(message);
    }
}
