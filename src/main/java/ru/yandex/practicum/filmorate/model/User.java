package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;

@Data
public class User {
    private int id;

    @Email(message = "Email адрес должен быть формата: \"email@email.email\"")
    private String email;

    private String login;

    private String name;

    private LocalDate birthday;

    public User(String email, String login, String name, String birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = LocalDate.parse(birthday);
    }
}
