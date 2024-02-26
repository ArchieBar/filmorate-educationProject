package ru.yandex.practicum.filmorate.storage.mpa;

import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.List;

public interface MpaStorage {
    public List<Mpa> getAllMpa();

    public Mpa getMpaById(Integer id);
}
