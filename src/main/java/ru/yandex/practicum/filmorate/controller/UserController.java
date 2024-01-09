package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 1;
    private final Map<Integer, User> users = new HashMap<>();

    private int createId() {
        return id++;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Вызов POST /users");
        if (checkValidityUser(user)) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        if (users.containsKey(user.getId())) {
            log.debug("Пользователь уже зарегистрирован: " + user.getEmail() +
                    " Все пользователи: " + List.copyOf(users.values()));
            throw new ValidationException("Пользователь с email адресом: " + user.getEmail() + " уже зарегистрирован");
        }
        user.setId(createId());
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) throws ValidationException {
        log.info("Вызов PUT /users");
        if (checkValidityUser(user)) {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
        if (!users.containsKey(user.getId())) {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private boolean checkValidityUser(User user) throws ValidationException {
        boolean flag = false;
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            flag = true;
            log.debug("Пустой или без символа: \"@\" email: " + user);
            throw new ValidationException("Email адрес не может быть пустым и должен содержать \"@\"");
        }
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isBlank()) {
            flag = true;
            log.debug("Пустой или содержащий пробелы логин: " + user);
            throw new ValidationException("Логин не может быть пустым и содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Пустое имя, установлен логин вместо имени: " + user);
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            flag = true;
            log.debug("Дата рождения находится в будущем: " + user + " Текущая дата: " + LocalDateTime.now());
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
        return flag;
    }
}
