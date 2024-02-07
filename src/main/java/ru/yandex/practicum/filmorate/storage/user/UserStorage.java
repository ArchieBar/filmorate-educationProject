package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserStorage {
    public User findUserByID(Integer userId);

    public List<User> getAllUser();

    public User createUser(User user);

    public User updateUser(User user);
}
