package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getFriendsUser(Integer userId) {
        User user = userStorage.findUserByID(userId);
        return user.getFriends().stream()
                .map(userStorage::findUserByID)
                .collect(Collectors.toList());
    }

    public List<User> addFriend(Integer userId, Integer otherUserId) {
        User user = userStorage.findUserByID(userId);
        User otherUser = userStorage.findUserByID(otherUserId);
        user.setFriend(otherUser);
        otherUser.setFriend(user);
        return user.getFriends().stream()
                .map(userStorage::findUserByID)
                .collect(Collectors.toList());
    }

    public List<User> deleteFriend(Integer userId, Integer otherUserId) {
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
        return userStorage.findUserByID(userId).getFriends().stream()
                .filter(id -> userStorage.findUserByID(otherUserId).getFriends().contains(id))
                .map(userStorage::findUserByID)
                .collect(Collectors.toList());
    }
}
