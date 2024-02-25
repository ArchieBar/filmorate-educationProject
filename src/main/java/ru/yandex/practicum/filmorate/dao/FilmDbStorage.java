package ru.yandex.practicum.filmorate.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Component("Film_DAO")
@Qualifier("Film_DAO")
@Primary
public class FilmDbStorage implements FilmStorage {
    private final Logger log = LoggerFactory.getLogger(FilmDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film findFilmById(Integer filmId) {
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE id_film = ?", filmId);
        if (filmRow.next()) {
            Film film = new Film(
                    filmRow.getInt("id_film"),
                    filmRow.getString("name"),
                    filmRow.getString("description"),
                    filmRow.getString("release_date"),
                    filmRow.getInt("duration"),
                    filmRow.getDouble("rate"),
                    (Mpa) filmRow.getObject("mpa"),
                    (Set<Genre>) filmRow.getObject("genres")
            );

            log.info("Найден фильм: {} {} ", film.getIdFilm(), film.getName());

            return film;
        } else {
            log.debug(MessageFormat.format("Фильм с id: {0} не найден", filmId) +
                    "\n" + getAllFilm());
            throw new FilmNotFountException(MessageFormat.format("Фильм с id: {0} не найден", filmId));
        }
    }

    @Override
    public List<Film> getAllFilm() {
        String sql = "SELECT * FROM film";

        return jdbcTemplate.query(sql, (rs, rowNom) -> makeFilm(rs));
    }

    @Override
    public Film createFilm(Film film) {
        SqlRowSet filmRow = jdbcTemplate.queryForRowSet("SELECT * FROM film WHERE id_film = ?", film.getIdFilm());
        String sqlInsert = "INSERT INTO film " +
                "(name, description, release_date, duration, rate, mpa, genres) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        if (filmRow.next()) {
            log.debug(MessageFormat.format("Фильм с id: {0} уже добавлен", film.getIdFilm()) +
                    "\n" + getAllFilm());
            throw new ValidationException(
                    MessageFormat.format("Фильм с id: {0} уже добавлен", film.getIdFilm()));
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                    PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, film.getName());
                    ps.setString(2, film.getDescription());
                    ps.setString(3, film.getReleaseDate().toString());
                    ps.setInt(4, film.getDuration());
                    ps.setDouble(5, film.getRate());
                    ps.setObject(6, film.getMpa());
                    ps.setObject(7, film.getGenres());
                    return ps;
            }, keyHolder);
            int generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return findFilmById(generatedId);
        }
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlUpdate =
                "UPDATE film SET " +
                        "name = ?, " +
                        "description = ?, " +
                        "release_date = ?, " +
                        "duration = ? " +
                        "rate = ? " +
                        "mpa = ? " +
                        "genres = ? " +
                        "WHERE id_film = ?";
        findFilmById(film.getIdFilm());
        jdbcTemplate.update(sqlUpdate,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa(),
                film.getGenres()
        );
        return findFilmById(film.getIdFilm());
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        int idFilm = rs.getInt("id_film");
        String name = rs.getString("name");
        String description = rs.getString("description");
        String releaseDate = rs.getString("release_date");
        int duration = rs.getInt("duration");
        double rate = rs.getDouble("rate");
        Mpa mpa = (Mpa) rs.getObject("mpa");
        Set<Genre> genres = (Set<Genre>) rs.getObject("genres");

        return new Film(
                idFilm,
                name,
                description,
                releaseDate,
                duration,
                rate,
                mpa,
                genres
        );
    }
}
