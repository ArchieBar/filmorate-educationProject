package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
public class User {
    private int id;

    @NotBlank(message = "Email адрес не может быть пустым")
    @Email(message = "Email адрес должен быть формата: \"example@gmail.com\"")
    private String email;

    @NotBlank(message = "Login пользователя не может быть пустым")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения пользователя не может быть в будущем")
    private LocalDate birthday;

    public User(String email, String login, String name, String birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = LocalDate.parse(birthday);
    }
}
