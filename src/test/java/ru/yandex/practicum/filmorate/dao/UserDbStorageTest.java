package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;

    @Test
    public void testFindUserById() {
        User newUser = new User(1, "test@mail.ru", "test_login", "Test", "1900-01-01");
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        userStorage.createUser(newUser);

        User savedUser = userStorage.findUserById(newUser.getId());

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testGetAllUser() {
        // Подготавливаем данные для теста
        User newUser1 = new User(3, "test@mail.ru", "test_login", "Test", "1900-01-01");
        User newUser2 = new User(4, "test2@mail.ru", "test_login2", "Test2", "1900-01-01");
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        userStorage.createUser(newUser1);
        userStorage.createUser(newUser2);

        List<User> users = userStorage.getAllUser();

        assertThat(users)
                .isNotNull()
                .hasSize(2)
                .containsExactlyInAnyOrder(newUser1, newUser2);
    }

    @Test
    public void testCreateUser() {
        User newUser = new User(3, "test3@mail.ru", "test_login3", "Test3", "1900-01-01");
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        User savedUser = userStorage.createUser(newUser);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testUpdateUser() {
        User newUser = new User(1, "test_updated@mail.ru", "test_login_updated", "Test_updated", "1900-01-01");
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        userStorage.createUser(newUser);

        User savedUser = userStorage.updateUser(newUser);

        assertThat(savedUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testCheckFriendshipStatus() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        int status = userStorage.checkFriendshipStatus(1, 2);

        assertThat(status).isEqualTo(1);
    }

    @Test
    public void testInsertFriendship() {
        User user = new User(1, "test_updated@mail.ru", "test_login_updated", "Test_updated", "1900-01-01");
        User friend = new User(3, "test_updated@mail.ru", "test_login_updated", "Test_updated", "1900-01-01");
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);
        userStorage.createUser(user);
        userStorage.createUser(friend);

        userStorage.insertFriendship(user.getId(), friend.getId());
        userStorage.insertFriendship(friend.getId(), user.getId());
        userStorage.updateFriendship(user.getId(), friend.getId(), 2);

        int status = userStorage.checkFriendshipStatus(user.getId(), friend.getId());
        assertThat(status).isEqualTo(2);
    }

    @Test
    public void testRemoveFriendship() {
        UserDbStorage userStorage = new UserDbStorage(jdbcTemplate);

        userStorage.removeFriendship(1, 2);

        int status = userStorage.checkFriendshipStatus(1, 2);
        assertThat(status).isEqualTo(1);
    }
}