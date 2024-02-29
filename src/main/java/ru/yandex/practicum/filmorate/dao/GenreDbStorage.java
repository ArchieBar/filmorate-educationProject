package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> getAllGenres() {
        String sql = "SELECT * FROM genres_list";
        return jdbcTemplate.query(sql, this::makeGenre);
    }

    @Override
    public Genre getGenreById(Integer id) {
        String sql = "SELECT * FROM genres_list WHERE id_genre = ?";
        List<Genre> result = jdbcTemplate.query(sql, this::makeGenre, id);
        if (result.isEmpty()) {
            log.debug(MessageFormat.format("Genre с id: {0} не найден", id));
            throw new GenreNotFoundException(MessageFormat.format("Genre с id: {0} не найден", id));
        } else {
            log.info("Найден genre: {} {} ", result.get(0).getId(), result.get(0).getName());
            return result.get(0);
        }
    }

    private Genre makeGenre(ResultSet rs, int rowNum) throws SQLException {
        return new Genre(rs.getInt("id_genre"), rs.getString("genre"));
    }
}
