package ru.yandex.practicum.filmorate.model.film;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@ToString
@RequiredArgsConstructor
public class Film {
    private int id;
    @NotBlank(message = "Название фильма не может быть пустым.")
    private String name;
    @Size(min = 0, max = 200, message = "Описание фильма не может быть больше 255 символов.")
    private String description;
    private LocalDate releaseDate;
    @Positive
    private int duration;
    private double rate;
    private Mpa mpa;
    private Set<Genre> genres = new HashSet<>();
    private Set<Integer> likes = new HashSet<>();

    public Film(
            int id,
            String name,
            String description,
            String releaseDate,
            int duration,
            double rate,
            Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = LocalDate.parse(releaseDate);
        this.duration = duration;
        this.rate = rate;
        this.mpa = mpa;
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
