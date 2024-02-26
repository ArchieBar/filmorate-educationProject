package ru.yandex.practicum.filmorate.storage.user;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("User_In_Memory")
public class InMemoryUserStorage implements UserStorage {
    @Getter
    private final Map<Integer, User> users = new HashMap<>();

    private int id = 1;

    private int createId() {
        return id++;
    }

    @Override
    public User findUserById(Integer userId) {
        if (!users.containsKey(userId)) {
            log.debug(MessageFormat.format("Пользователь с id: {0} не найден", userId) +
                    "\n" + users);
            throw new UserNotFountException(MessageFormat.format("Пользователь с id: {0} не найден", userId));
        }
        return users.get(userId);
    }

    @Override
    public List<User> getAllUser() {
        return List.copyOf(users.values());
    }

    @Override
    public User createUser(User user) {
        log.info("Вызов метода /createUser - " + user);
        if (users.containsKey(user.getId())) {
            log.debug(MessageFormat.format("Пользователь с id: {0} уже зарегистрирован", user.getId()) +
                    "\n" + users);
            throw new ValidationException(
                    MessageFormat.format("Пользователь с id: {0} уже зарегистрирован", user.getId()));
        }
        user.setId(createId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.info("Вызов метода /updateUser - " + user);
        findUserById(user.getId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public int checkFriendshipStatus(int id, int idFriend) {
        return 0;
    }

    @Override
    public void updateFriendship(int id, int idFriend, int idStatus) {

    }

    @Override
    public void insertFriendship(int id, int idFriend) {

    }

    @Override
    public void removeFriendship(int id, int idFriend) {

    }
}
