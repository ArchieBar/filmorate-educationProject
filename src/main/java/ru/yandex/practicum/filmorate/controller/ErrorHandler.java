package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse validationHandle(Exception e) {
        log.debug("Ошибка валидации: " + e.getMessage());
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler({
            FilmCreatePreviouslyException.class,
            UserCreatePreviouslyException.class,
            LikeCreatePreviouslyException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private ErrorResponse previouslyCreateObject(Exception e) {
        log.debug("Ранее созданный объект: " + e.getMessage());
        return new ErrorResponse("Ранее созданный объект", e.getMessage());
    }

    @ExceptionHandler({
            FilmNotFountException.class,
            UserNotFountException.class,
            MpaNotFoundException.class,
            GenreNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse filmNotFountHandle(Exception e) {
        log.debug("Не найдено: " + e.getMessage());
        return new ErrorResponse("Не найдено", e.getMessage());
    }

    @ExceptionHandler({SQLException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    private ErrorResponse sqlExceptionHandler(Exception e) {
        log.debug("Ошибка с базой данных: " + e.getMessage());
        return new ErrorResponse("Ошибка с базой данных", e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private ErrorResponse throwableHandle(Throwable e) {
        log.debug("Ошибка сервера: " + e.getMessage());
        return new ErrorResponse("Ошибка сервера", e.getMessage());
    }
}
