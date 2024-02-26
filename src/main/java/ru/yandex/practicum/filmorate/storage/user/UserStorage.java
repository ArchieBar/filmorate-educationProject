package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserStorage {
    public User findUserById(Integer userId);

    public List<User> getAllUser();

    public User createUser(User user);

    public User updateUser(User user);

    public int checkFriendshipStatus(int id, int idFriend);

    public void updateFriendship(int id, int idFriend, int id_status);

    public void insertFriendship(int id, int idFriend);

    public void removeFriendship(int id, int idFriend);
}
