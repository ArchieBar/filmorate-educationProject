package ru.yandex.practicum.filmorate.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int id = 1;
    private final ObjectMapper mapper;

    public UserController (ObjectMapper mapper) {
        this.mapper = mapper;
    }

    private int createId() {
        return id++;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return List.copyOf(users.values());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        log.info("Вызов POST /users: " + user);
        validateUserName(user);
        if (users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь с id: " + user.getId() + " уже зарегистрирован");
        }
        user.setId(createId());
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        log.info("Вызов PUT /users: " + user);
        validateUserName(user);
        if (!users.containsKey(user.getId())) {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        users.put(user.getId(), user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    private void validateUserName(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Пустое имя, установлен логин вместо имени: " + user);
            user.setName(user.getLogin());
        }
    }
}
