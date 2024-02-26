package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFountException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage; // @Primary in UserDbStorage (User_DAO)

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    private User validateUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            log.debug("Пустое имя, установлен логин вместо имени: " + user);
            user.setName(user.getLogin());
        }
        return user;
    }

    public User findUserByID(Integer userId) {
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
        User user = userStorage.findUserById(userId);
        return user.getFriends().stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
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
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(idFriend);
        if (user == null) {
            log.info(MessageFormat.format("Пользователь с id: {0} не найден", id));
            throw new UserNotFountException(MessageFormat.format("Пользователь с id: {0} не найден", id));
        } else if (friend == null) {
            log.info(MessageFormat.format("Пользователь с id: {0} не найден", idFriend));
            throw new UserNotFountException(MessageFormat.format("Пользователь с id: {0} не найден", idFriend));
        } else if (user.getFriends().contains(idFriend)) {
            log.info(MessageFormat.format("Пользователь с id: {0} уже добавлен в друзья", idFriend));
        }

        user.setFriend(friend);

        switch (userStorage.checkFriendshipStatus(id, idFriend)) {
            case 1:
                userStorage.updateFriendship(id, idFriend, 2);
            case 2:
                userStorage.insertFriendship(id, idFriend);
        }

        return user.getFriends().stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> deleteFriend(Integer id, Integer idFriend) {
        log.info(MessageFormat.format(
                "Вызов метода: /deleteFriend - userId: {0}, otherUserId: {1}", id, idFriend));
        User user = userStorage.findUserById(id);
        User friend = userStorage.findUserById(idFriend);
        if (user == null) {
            log.info(MessageFormat.format("Пользователь с id: {0} не найден", id));
            throw new UserNotFountException(MessageFormat.format("Пользователь с id: {0} не найден", id));
        } else if (friend == null) {
            log.info(MessageFormat.format("Пользователь с id: {0} не найден", idFriend));
            throw new UserNotFountException(MessageFormat.format("Пользователь с id: {0} не найден", idFriend));
        } else if (user.getFriends().contains(idFriend)) {
            log.info(MessageFormat.format("Пользователь с id: {0} уже добавлен в друзья", idFriend));
        }

        user.removeFriend(friend);

        switch (userStorage.checkFriendshipStatus(id, idFriend)) {
            case 1:
                userStorage.removeFriendship(id, idFriend);
            case 2:
                userStorage.updateFriendship(id, idFriend, 1);
        }

        return user.getFriends().stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> findMutualFriends(Integer userId, Integer otherUserId) {
        Set<Integer> userFriends = userStorage.findUserById(userId).getFriends();
        Set<Integer> otherUserFriends = userStorage.findUserById(otherUserId).getFriends();
        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }
}
