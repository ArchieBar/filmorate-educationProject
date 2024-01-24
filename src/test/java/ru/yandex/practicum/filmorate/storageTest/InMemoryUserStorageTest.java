package ru.yandex.practicum.filmorate.storageTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryUserStorageTest {
    private UserStorage userStorage;
    User user1;
    User user2;
    User user3;

    @BeforeEach
    void setUp() {
        this.userStorage = new InMemoryUserStorage();

        user1 = new User("example@gmail.com", "login1", "name1", "2001-01-01");
        user2 = new User("example@gmail.com", "login2", "name2", "2002-02-02");
        user3 = new User("example@gmail.com", "login3", "name3", "2003-03-03");

        userStorage.createUser(user1); // id = 1
        userStorage.createUser(user2); // id = 2
        userStorage.createUser(user3); // id = 3
    }


    @Test
    void getAllUser() {
        assertEquals(3, userStorage.getAllUser().size());
    }

    @Test
    void createUser() {
        assertEquals(user1, userStorage.findUserByID(1));
        assertEquals(user2, userStorage.findUserByID(2));
        assertEquals(user3, userStorage.findUserByID(3));
    }

    @Test
    void updateUser() {
        User newUser = new User("example@gmail.com", "loginNew", "nameNew", "2001-01-01");
        newUser.setId(1);

        userStorage.updateUser(newUser);

        assertEquals(newUser, userStorage.findUserByID(1));
    }
}