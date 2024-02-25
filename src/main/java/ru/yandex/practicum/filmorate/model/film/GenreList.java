package ru.yandex.practicum.filmorate.model.film;

import java.util.HashMap;
import java.util.Map;

public class GenreList {
    private final Map<Integer, String> genreList = new HashMap<>();

    public GenreList() {
        genreList.put(1, "Боевик");
        genreList.put(2, "Комедия");
        genreList.put(3, "Драма");
        genreList.put(4, "Фантастика");
        genreList.put(5, "Ужасы");
        genreList.put(6, "Триллер");
        genreList.put(7, "Приключения");
        genreList.put(8, "Фэнтези");
        genreList.put(9, "Вестерн");
    }

    public String getGenreById(Integer idGenre) {
        return genreList.get(idGenre);
    }
}
