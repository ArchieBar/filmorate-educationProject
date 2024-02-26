package ru.yandex.practicum.filmorate.model.film;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Mpa implements Serializable {
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
