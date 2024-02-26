package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@Component
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sql = "SELECT * FROM mpa_list";
        return jdbcTemplate.query(sql, this::makeMpa);
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sql = "SELECT * FROM mpa_list WHERE id_mpa = ?";
        List<Mpa> result = jdbcTemplate.query(sql, this::makeMpa, id);
        if (result.isEmpty()) {
            log.debug(MessageFormat.format("Mpa с id: {0} не найден", id));
            throw new MpaNotFoundException(MessageFormat.format("Mpa с id: {0} не найден", id));
        } else {
            log.info("Найден mpa: {} {} ", result.get(0).getId(), result.get(0).getName());
            return result.get(0);
        }
    }

    private Mpa makeMpa(ResultSet rs, int rowNum) throws SQLException {
        return new Mpa(rs.getInt("id_mpa"), rs.getString("mpa"));
    }
}
