package ru.yandex.practicum.filmorate.dao;

import org.aspectj.lang.annotation.SuppressAjWarnings;
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
import ru.yandex.practicum.filmorate.exception.UserNotFountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Component("User_DAO")
@Qualifier("Film_DAO")
@Primary
public class UserDbStorage implements UserStorage {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findUserById(Integer userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE id_user = ?", userId);
        if (userRows.next()) {
            User user = new User(
                    userRows.getInt("id_user"),
                    userRows.getString("email"),
                    userRows.getString("login"),
                    userRows.getString("name"),
                    userRows.getString("birthday")
            );

            log.info("Найден пользователь: {} {} ", user.getId_user(), user.getLogin());

            return user;
        } else {
            log.debug(MessageFormat.format("Пользователь с id: {0} не найден", userId) +
                    "\n" + getAllUser());
            throw new UserNotFountException(MessageFormat.format("Пользователь с id: {0} не найден", userId));
        }
    }

    @Override
    public List<User> getAllUser() {
        String sql = "SELECT * FROM users";

        return jdbcTemplate.query(sql, (rs, rowNom) -> makeUser(rs));
    }

    @Override
    public User createUser(User user) {
        String sqlInsert = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?)";

        if (findUserById(user.getId_user()) != null) {
            log.debug(MessageFormat.format("Пользователь с id: {0} уже зарегистрирован", user.getId_user()) +
                    "\n" + getAllUser());
            throw new ValidationException(
                    MessageFormat.format("Пользователь с id: {0} уже зарегистрирован", user.getId_user()));
        } else {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(con -> {
                PreparedStatement ps = con.prepareStatement(sqlInsert, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getEmail());
                ps.setString(2, user.getLogin());
                ps.setString(3, user.getName());
                ps.setString(4, user.getBirthday().toString());
                return ps;
            }, keyHolder);
            int generatedId = Objects.requireNonNull(keyHolder.getKey()).intValue();
            return findUserById(generatedId);
        }
    }

    @Override
    public User updateUser(User user) {
        String sqlUpdate = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id_user = ?";
        findUserById(user.getId_user());
        jdbcTemplate.update(sqlUpdate,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId_user());
        return findUserById(user.getId_user());
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int idUser = rs.getInt("id_user");
        String email = rs.getString("email");
        String login = rs.getString("login");
        String name = rs.getString("name");
        String birthday = rs.getString("birthday");

        return new User(idUser, email, login, name, birthday);
    }
}
