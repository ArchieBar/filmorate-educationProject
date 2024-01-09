package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void methodForGettingAllUsersShouldReturnCorrectNumberOfUsers() throws ValidationException {
        User user1 = new User("email_1@email.ru", "login", "name", "2000-01-01");
        User user2 = new User("email_2@email.ru", "login", "name", "2000-01-01");
        User user3 = new User("email_3@email.ru", "login", "name", "1946-08-20");

        userController.createUser(user1);
        userController.createUser(user2);
        userController.createUser(user3);

        assertEquals(3, userController.getAllUsers().size());
    }

    @Test
    void createdUserMustMatchTheUserInMemory() throws ValidationException {
        User user1 = new User("email_1@email.ru", "login", "name", "2000-01-01");

        userController.createUser(user1);

        assertEquals(user1, userController.getAllUsers().get(0));
    }

    @Test
    void afterUpdatingTheUserOnlyTheUpdatedUserShouldBeStoredInMemory() throws ValidationException {
        User user = new User("email@email.ru", "login", "name", "2000-01-01");
        User newUser = new User("email@email.ru", "newLogin", "name", "2000-01-01");

        userController.createUser(user);
        newUser.setId(user.getId());
        userController.updateUser(newUser);

        assertEquals(1, userController.getAllUsers().size());
        assertEquals(newUser, userController.getAllUsers().get(0));
    }

    @Test
    void ifYouTryToCreateAUserWithIncorrectEmailAnExceptionShouldBeThrown() throws ValidationException {
        User emailWithoutChar = new User("emailemail.ru", "login", "name", "2000-01-01");
        User emailBlank = new User(" ", "login", "name", "2000-01-01");
        User emailEmpty = new User(null, "login", "name", "2000-01-01");
        User emailNormalUser = new User("email@mail.ru", "login", "name", "2000-01-01");

        final ValidationException exceptionEmailWithoutChar = assertThrows(ValidationException.class,
                () -> userController.createUser(emailWithoutChar));
        final ValidationException exceptionEmailEmpty = assertThrows(ValidationException.class,
                () -> userController.createUser(emailEmpty));
        final ValidationException exceptionEmailBlank = assertThrows(ValidationException.class,
                () -> userController.createUser(emailBlank));
        userController.createUser(emailNormalUser);

        assertEquals("Email адрес не может быть пустым и должен содержать \"@\"",
                exceptionEmailWithoutChar.getMessage());
        assertEquals("Email адрес не может быть пустым и должен содержать \"@\"",
                exceptionEmailEmpty.getMessage());
        assertEquals("Email адрес не может быть пустым и должен содержать \"@\"",
                exceptionEmailBlank.getMessage());
        assertEquals(1, userController.getAllUsers().size());
        assertEquals(emailNormalUser, userController.getAllUsers().get(0));
    }

    @Test
    void ifYouTryToCreateAUserWithIncorrectLoginAnExceptionShouldBeThrown() {
        User loginNull = new User("email@mail.ru", null, "name", "2000-01-01");
        User loginBlank = new User("email@mail.ru", " ", "name", "2000-01-01");
        User loginEmpty = new User("email@mail.ru", "", "name", "2000-01-01");

        final ValidationException exceptionLoginNull = assertThrows(ValidationException.class,
                () -> userController.createUser(loginNull));
        final ValidationException exceptionLoginBlank = assertThrows(ValidationException.class,
                () -> userController.createUser(loginBlank));
        final ValidationException exceptionLoginEmpty = assertThrows(ValidationException.class,
                () -> userController.createUser(loginEmpty));

        assertEquals("Логин не может быть пустым и содержать пробелы",
                exceptionLoginNull.getMessage());
        assertEquals("Логин не может быть пустым и содержать пробелы",
                exceptionLoginBlank.getMessage());
        assertEquals("Логин не может быть пустым и содержать пробелы",
                exceptionLoginEmpty.getMessage());
    }

    @Test
    void ifYouTryToCreateAUserWithIncorrectNameAnExceptionShouldBeThrown() throws ValidationException {
        User nameNullName = new User("nameNullName@mail.ru", "login", null, "2000-01-01");
        User nameEmptyName = new User("nameEmptyName@mail.ru", "login", "", "2000-01-01");
        User nameBlankName = new User("nameBlankName@mail.ru", "login", " ", "2000-01-01");

        userController.createUser(nameNullName);
        userController.createUser(nameEmptyName);
        userController.createUser(nameBlankName);

        assertEquals("login", userController.getAllUsers().get(0).getName());
        assertEquals("login", userController.getAllUsers().get(1).getName());
        assertEquals("login", userController.getAllUsers().get(2).getName());
    }

    @Test
    void ifYouTryToCreateAUserWithIncorrectBirthdayAnExceptionShouldBeThrown() throws ValidationException {
        User nowBirthday = new User("nowBirthday@mail.ru", "login", "name", LocalDate.now().toString());
        User normalBirthday = new User("normalBirthday@mail.ru", "login", "name", "2000-01-01");
        User futureBirthday = new User("futureBirthday@mail.ru", "login", "name", "3000-01-01");

        userController.createUser(nowBirthday);
        final ValidationException exceptionFutureBirthday = assertThrows(ValidationException.class,
                () -> userController.createUser(futureBirthday));
        userController.createUser(normalBirthday);

        assertEquals("Дата рождения не может быть в будущем.", exceptionFutureBirthday.getMessage());
        assertEquals(2, userController.getAllUsers().size());
    }
}