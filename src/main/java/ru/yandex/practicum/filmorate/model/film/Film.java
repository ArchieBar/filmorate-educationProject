package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private int idFilm;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(min = 0, max = 255, message = "Описание фильма не может быть больше 255 символов.")
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private double rate;
    private Mpa mpa;
    private Set<Genre> genres;
    private Set<Integer> likes;

    public Film() {
    }

    public Film(
            String name,
            String description,
            String releaseDate,
            int duration,
            double rate,
            Mpa mpa,
            Set<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = LocalDate.parse(releaseDate);
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = new HashSet<>();
    }

    public Film(
            int idFilm,
            String name,
            String description,
            String releaseDate,
            int duration,
            double rate,
            Mpa mpa,
            Set<Genre> genres) {
        this.idFilm = idFilm;
        this.name = name;
        this.description = description;
        this.releaseDate = LocalDate.parse(releaseDate);
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = new HashSet<>();
    }

    public Film(
            int idFilm,
            String name,
            String description,
            String releaseDate,
            int duration,
            double rate,
            Mpa mpa,
            Set<Genre> genres,
            Set<Integer> likes) {
        this.idFilm = idFilm;
        this.name = name;
        this.description = description;
        this.releaseDate = LocalDate.parse(releaseDate);
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
        this.genres = genres;
        this.likes = likes;
    }

    public int getCountLikes() {
        if  (likes.isEmpty()) {
            return 0;
        } else {
            return likes.size();
        }
    }

    public void setLike(Integer userId) {
        likes.add(userId);
    }

    public void removeLike(Integer userId) {
        likes.remove(userId);
    }
}
