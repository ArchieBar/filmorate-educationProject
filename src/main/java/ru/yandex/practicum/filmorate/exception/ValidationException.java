package ru.yandex.practicum.filmorate.exception;

import java.io.IOException;

public class ValidationException extends IOException {
    public ValidationException() {
        super();
    }

    public ValidationException(String message) {
       super(message);
    }
}
