package ru.yandex.practicum.filmorate.exception;

public class UserCreatePreviouslyException extends RuntimeException {
    public UserCreatePreviouslyException(String message) {
        super(message);
    }
}
