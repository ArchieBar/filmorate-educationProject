package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFountException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private User validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.info("Пустое имя, установлен логин вместо имени: " + user);
            user.setName(user.getLogin());
        }
        return user;
    }

    public User findUserById(Integer userId) {
        User user = userStorage.findUserById(userId);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFountException(MessageFormat.format("Пользователь с id: {0} не найден", userId));
        }
    }

    public List<User> getAllUser() {
        return userStorage.getAllUser();
    }

    public List<User> getFriendsUser(Integer userId) {
        return userStorage.getFriendsUser(userId);
    }

    public User createUser(User user) {
        log.info("Вызов метода: /createUser - " + user);
        return userStorage.createUser(validateUserName(user));
    }

    public User updateUser(User user) {
        log.info("Вызов метода: /updateUser - " + user);
        return userStorage.updateUser(validateUserName(user));
    }

    public List<User> addFriend(Integer id, Integer idFriend) {
        log.info(MessageFormat.format(
                "Вызов метода: /addFriend - userId: {0}, otherUserId: {1}", id, idFriend));
        User user = findUserById(id);
        User friend = findUserById(idFriend);
        if (user.getFriends().contains(idFriend)) {
            log.info(MessageFormat.format("Пользователь с id: {0} уже добавлен в друзья", idFriend));
            return getFriendsUser(id);
        }

        user.setFriend(friend);

        switch (userStorage.checkFriendshipStatus(id, idFriend)) {
            case 1:
                userStorage.updateFriendship(id, idFriend, 2);
            case 2:
                userStorage.insertFriendship(id, idFriend);
        }

        return getFriendsUser(id);
    }

    public List<User> deleteFriend(Integer id, Integer idFriend) {
        log.info(MessageFormat.format(
                "Вызов метода: /deleteFriend - userId: {0}, otherUserId: {1}", id, idFriend));
        User user = findUserById(id);
        User friend = findUserById(idFriend);

        user.removeFriend(friend);

        switch (userStorage.checkFriendshipStatus(id, idFriend)) {
            case 1:
                userStorage.removeFriendship(id, idFriend);
            case 2:
                userStorage.updateFriendship(id, idFriend, 1);
        }

        return getFriendsUser(id);
    }

    public List<User> findMutualFriends(Integer userId, Integer otherUserId) {
        findUserById(userId);
        findUserById(otherUserId);
        return userStorage.findMutualFriends(userId, otherUserId);
    }
}
