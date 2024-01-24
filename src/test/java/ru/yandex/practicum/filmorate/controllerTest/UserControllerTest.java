package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserControllerTest {
    UserController userController;
    Validator validator;

    @BeforeEach
    void sutUp() {
        userController = new UserController(new InMemoryUserStorage());
        validator = Validation.buildDefaultValidatorFactory().getValidator();
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
        User emailWithoutChar = new User("email.ru", "login", "name", "2000-01-01");
        User emailBlank = new User(" ", "login", "name", "2000-01-01");
        User emailEmpty = new User(null, "login", "name", "2000-01-01");
        User emailBadChar = new User("emailMail.ru@", "login", "name", "2000-01-01");
        User emailNormal = new User("email@mail.ru", "login", "name", "2000-01-01");

        User responseEmailNormal = userController.createUser(emailNormal);

        assertFalse(validator.validate(emailWithoutChar).isEmpty());
        assertFalse(validator.validate(emailBlank).isEmpty());
        assertFalse(validator.validate(emailEmpty).isEmpty());
        assertFalse(validator.validate(emailBadChar).isEmpty());
        assertEquals(emailNormal, responseEmailNormal);
    }

    @Test
    void ifYouTryToCreateAUserWithIncorrectLoginAnExceptionShouldBeThrown() {
        User loginNull = new User("email@mail.ru", null, "name", "2000-01-01");
        User loginBlank = new User("email@mail.ru", " ", "name", "2000-01-01");
        User loginEmpty = new User("email@mail.ru", "", "name", "2000-01-01");

        assertFalse(validator.validate(loginNull).isEmpty());
        assertFalse(validator.validate(loginBlank).isEmpty());
        assertFalse(validator.validate(loginEmpty).isEmpty());
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

        User responseNowBirthday = userController.createUser(nowBirthday);
        User responseNormalBirthday = userController.createUser(normalBirthday);

        assertFalse(validator.validate(futureBirthday).isEmpty());
        assertEquals(nowBirthday, responseNowBirthday);
        assertEquals(normalBirthday, responseNormalBirthday);
    }
}