package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<String, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) throws ValidationException {
        log.info("Вызов POST /users");
        try {
            checkValidityUser(user);
            if (users.containsKey(user.getEmail())) {
                log.debug("Пользователь уже зарегистрирован: " + user.getEmail() +
                        " Все пользователи: " + List.copyOf(users.values()));
                throw new ValidationException("Пользователь с email адресом: " + user.getEmail() + " уже зарегистрирован");
            }
        } catch (ValidationException exception) {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        users.put(user.getEmail(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        log.info("Вызов PUT /users");
        try {
            checkValidityUser(user);
        } catch (ValidationException exception) {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        users.put(user.getEmail(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private void checkValidityUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.debug("Пустой или не содержащий \"@\": " + user);
            throw new ValidationException("Email адрес не может быть пустым и должен содержать \"@\"");
        }
        if (user.getLogin() == null || !user.getLogin().contains(" ")) {
            log.debug("Пустой или содержащий пробелы логин: " + user);
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Пустое имя, установить логин вместо имени: " + user);
            //TODO Возвращается правильный юзер?
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDateTime.now())) {
            log.debug("Дата рождения находится в будущем: " + user + " Текущая дата: " + LocalDateTime.now());
            throw new ValidationException("Дата рождения не может быть в будущем. " +
                    "Дата рождения: " + user.getBirthday().format(DateTimeFormatter.ISO_DATE));
        }
    }
}
