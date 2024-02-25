package ru.yandex.practicum.filmorate.model.film;

import java.util.HashMap;
import java.util.Map;

public class RatingList {
    private final Map<Integer, String> ratingList = new HashMap<>();

    public RatingList() {
        ratingList.put(1, "G");
        ratingList.put(2, "PG");
        ratingList.put(3, "PG-13");
        ratingList.put(4, "R");
        ratingList.put(5, "NC-17");
    }

    public String getRatingById(Integer idRating) {
        return ratingList.get(idRating);
    }
}
