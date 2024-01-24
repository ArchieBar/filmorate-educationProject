package ru.yandex.practicum.filmorate.exception;

public class UserNotFountException extends RuntimeException {
    public UserNotFountException(String message) {
        super(message);
    }
}
