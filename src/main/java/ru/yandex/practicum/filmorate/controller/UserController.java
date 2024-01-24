package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    //FIXME
    // С этим есть небольшие вопросы
    @Autowired
    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
        this.userService = new UserService(userStorage);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Integer userId) {
        return userStorage.findUserByID(userId);
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
        return userStorage.getAllUser();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.createUser(user);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);
    }

    @PutMapping("/{userId}/friends/{otherUserId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> addFriends(@PathVariable Map<String, String> pathUserId) {
        Integer userId = Integer.parseInt(pathUserId.get("userId"));
        Integer otherUserId = Integer.parseInt(pathUserId.get("otherUserId"));
        return userService.addFriend(userId, otherUserId);
    }

    @DeleteMapping("/{userId}/friends/{otherUserId}")
    @ResponseStatus(HttpStatus.OK)
    public List<User> deleteFriend(@PathVariable Map<String, String> pathUserId) {
        Integer userId = Integer.parseInt(pathUserId.get("userId"));
        Integer otherUserId = Integer.parseInt(pathUserId.get("otherUserId"));
        return userService.deleteFriend(userId, otherUserId);
    }
}
