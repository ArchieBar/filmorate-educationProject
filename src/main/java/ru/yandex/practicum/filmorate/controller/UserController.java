package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService; // @Primary UserDbStorage

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Integer userId) {
        return userService.findUserByID(userId);
    }

    @GetMapping("/{userId}/friends")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getFriendsUser(@PathVariable Integer userId) {
        return userService.getFriendsUser(userId);
    }

    @GetMapping("/{userId}/friends/common/{otherUserId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> getMutualFriends(@PathVariable Map<String, String> pathUserId) {
        Integer userId = Integer.parseInt(pathUserId.get("userId"));
        Integer otherUserId = Integer.parseInt(pathUserId.get("otherUserId"));
        return userService.findMutualFriends(userId, otherUserId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        log.info("Вызов метода POST: /createUser - " + user);
        return userService.createUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Вызов метода PUT: /updateUser - " + user);
        return userService.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{otherUserId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> addFriend(@PathVariable Map<String, String> pathUserId) {
        log.info("Вызов метода PUT: /addFriend - " + pathUserId);
        Integer userId = Integer.parseInt(pathUserId.get("userId"));
        Integer otherUserId = Integer.parseInt(pathUserId.get("otherUserId"));
        return userService.addFriend(userId, otherUserId);
    }

    @DeleteMapping("/{userId}/friends/{otherUserId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> deleteFriend(@PathVariable Map<String, String> pathUserId) {
        log.info("Вызов метода DELETE: /deleteFriend - " + pathUserId);
        Integer userId = Integer.parseInt(pathUserId.get("userId"));
        Integer otherUserId = Integer.parseInt(pathUserId.get("otherUserId"));
        return userService.deleteFriend(userId, otherUserId);
    }
}
