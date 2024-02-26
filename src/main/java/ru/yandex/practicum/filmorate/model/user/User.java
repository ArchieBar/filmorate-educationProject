package ru.yandex.practicum.filmorate.model.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@RequiredArgsConstructor
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
    private Set<Integer> friends = new HashSet<>();

    public User(String email, String login, String name, String birthday) {
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = LocalDate.parse(birthday);
    }

    public User(int id, String email, String login, String name, String birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = LocalDate.parse(birthday);
    }

    public void setFriend(User user) {
        friends.add(user.getId());
    }

    public void removeFriend(User user) {
        friends.remove(user.getId());
    }
}
