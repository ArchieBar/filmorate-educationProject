package ru.yandex.practicum.filmorate.model.film;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Genre implements Comparable<Genre> {
    private Integer id;
    private String name;

    public Genre() {
        this.name = "";
    }

    public Genre(Integer id) {
        this.id = id;
        this.name = "";
    }

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }


    @Override
    public int compareTo(Genre o) {
        return id.compareTo(o.id);
    }
}
