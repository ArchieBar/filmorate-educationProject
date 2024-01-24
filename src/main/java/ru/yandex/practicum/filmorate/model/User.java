package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    private Set<Integer> friends;

    public User(String email, String login, String name, String birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = LocalDate.parse(birthday);
        this.friends = new HashSet<>();
    }

    public void setFriend(User user) {
        friends.add(user.getId());
    }

    public void removeFriend(User user) {
        friends.remove(user.getId());
    }
}
