package ru.yandex.practicum.filmorate.serviceTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(new InMemoryUserStorage());

        User user1 = new User("example@gmail.com", "login1", "name1", "2001-01-01");
        User user2 = new User("example@gmail.com", "login2", "name2", "2002-02-03");
        User user3 = new User("example@gmail.com", "login3", "name3", "2002-03-03");

        userService.createUser(user1); // id = 1
        userService.createUser(user2); // id = 2
        userService.createUser(user3); // id = 3
    }

    @Test
    void addFriend() {
        userService.addFriend(1, 2);
        userService.addFriend(2, 3);
        userService.addFriend(3, 1);

        assertEquals(2, userService.getFriendsUser(1).size());
        assertEquals(2, userService.getFriendsUser(2).size());
        assertEquals(2, userService.getFriendsUser(3).size());
    }

    @Test
    void deleteFriend() {
        userService.addFriend(1, 2);
        userService.addFriend(2, 3);
        userService.addFriend(3, 1);

        userService.deleteFriend(1, 2);
        userService.deleteFriend(2, 3);

        assertEquals(1, userService.getFriendsUser(1).size());
        assertEquals(0, userService.getFriendsUser(2).size());
        assertEquals(1, userService.getFriendsUser(3).size());
    }

    @Test
    void findMutualFriends() {
        userService.addFriend(1, 3);
        userService.addFriend(2, 3);

        Integer mutualFriend = userService.findMutualFriends(1, 2).get(0).getId();

        assertEquals(3, mutualFriend);
    }
}