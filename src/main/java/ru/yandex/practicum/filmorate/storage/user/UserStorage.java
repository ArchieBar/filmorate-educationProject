package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserStorage {
    User findUserById(Integer userId);

    List<User> getAllUser();

    User createUser(User user);

    User updateUser(User user);

    int checkFriendshipStatus(int id, int idFriend);

    void updateFriendship(int id, int idFriend, int idStatus);

    void insertFriendship(int id, int idFriend);

    void removeFriendship(int id, int idFriend);
}
