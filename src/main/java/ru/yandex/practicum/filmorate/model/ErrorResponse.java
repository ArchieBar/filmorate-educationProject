package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    private final String error;

    private final String errorMessage;

    public ErrorResponse(String error, String errorMessage) {
        this.error = error;
        this.errorMessage = errorMessage;
    }

    public String getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
