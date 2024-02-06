package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private int id;

    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;

    @Size(min = 0, max = 200, message = "Описание фильма не может быть больше 200 символов.")
    private String description;

    private LocalDate releaseDate;

    @Positive
    private int duration;

    private Set<Integer> likes;

    private GenreFilm genre;

    private RatingFilm rating;

    public Film(
            String name,
            String description,
            String releaseDate,
            int duration,
            GenreFilm genre,
            RatingFilm rating) {
        this.name = name;
        this.description = description;
        this.releaseDate = LocalDate.parse(releaseDate);
        this.duration = duration;
        this.likes = new HashSet<>();
        this.genre = genre;
        this.rating = rating;
    }

    public int getCountLikes() {
        return likes.size();
    }

    public void setLike(Integer userId) {
        likes.add(userId);
    }

    public void removeLike(Integer userId) {
        likes.remove(userId);
    }
}
