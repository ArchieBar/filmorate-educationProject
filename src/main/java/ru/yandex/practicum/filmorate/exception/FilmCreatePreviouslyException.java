package ru.yandex.practicum.filmorate.exception;

public class FilmCreatePreviouslyException extends RuntimeException {
    public FilmCreatePreviouslyException(String message) {
        super(message);
    }
}
