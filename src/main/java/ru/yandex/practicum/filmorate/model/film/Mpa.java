package ru.yandex.practicum.filmorate.model.film;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mpa {
    private int id;
    private String name;

    public Mpa() {
        this.name = "";
    }

    public Mpa(int id) {
        this.id = id;
        this.name = "";
    }

    public Mpa(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
