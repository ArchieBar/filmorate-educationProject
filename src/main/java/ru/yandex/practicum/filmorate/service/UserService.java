package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
            log.debug("Пустое имя, установлен логин вместо имени: " + user);
            user.setName(user.getLogin());
        }
        return user;
    }

    public User findUserByID(Integer userId) {
        return userStorage.findUserByID(userId);
    }

    public List<User> getAllUser() {
        return userStorage.getAllUser();
    }

    public List<User> getFriendsUser(Integer userId) {
        User user = userStorage.findUserByID(userId);
        return user.getFriends().stream()
                .map(userStorage::findUserByID)
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

    public List<User> addFriend(Integer userId, Integer otherUserId) {
        log.info(MessageFormat.format(
                "Вызов метода: /addFriend - userId: {0}, otherUserId: {1}", userId, otherUserId));
        User user = userStorage.findUserByID(userId);
        User otherUser = userStorage.findUserByID(otherUserId);
        user.setFriend(otherUser);
        otherUser.setFriend(user);
        return user.getFriends().stream()
                .map(userStorage::findUserByID)
                .collect(Collectors.toList());
    }

    public List<User> deleteFriend(Integer userId, Integer otherUserId) {
        log.info(MessageFormat.format(
                "Вызов метода: /deleteFriend - userId: {0}, otherUserId: {1}", userId, otherUserId));
        User user = userStorage.findUserByID(userId);
        User otherUser = userStorage.findUserByID(otherUserId);
        user.removeFriend(otherUser);
        otherUser.removeFriend(user);
        return user.getFriends().stream()
                .map(userStorage::findUserByID)
                .collect(Collectors.toList());
    }

    //FIXME
    // Работает ли? :)
    // - Не работает :(
    // -- А нет, работает :)
    // 24.01.24 - "Это была крутая бессонная ночь"
    public List<User> findMutualFriends(Integer userId, Integer otherUserId) {
        Set<Integer> userFriends = userStorage.findUserByID(userId).getFriends();
        Set<Integer> otherUserFriends = userStorage.findUserByID(otherUserId).getFriends();
        return userFriends.stream()
                .filter(otherUserFriends::contains)
                .map(userStorage::findUserByID)
                .collect(Collectors.toList());
    }
}
