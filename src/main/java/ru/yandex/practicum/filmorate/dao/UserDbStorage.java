package ru.yandex.practicum.filmorate.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserCreatePreviouslyException;
import ru.yandex.practicum.filmorate.exception.UserNotFountException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@Primary
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findUserById(Integer idUser) {
        String sql = "SELECT * FROM users WHERE id_user = ?";
        List<User> result = jdbcTemplate.query(sql, this::makeUser, idUser);
        if (result.isEmpty()) {
            log.debug(MessageFormat.format("Пользователь с id: {0} не найден", idUser));
            return null;
        } else {
            log.info("Найден пользователь: {} {} ", result.get(0).getId(), result.get(0).getLogin());
            return result.get(0);
        }
    }

    @Override
    public List<User> getAllUser() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::makeUser);
    }

    @Override
    public User createUser(User user) {
        if (findUserById(user.getId()) == null) {
            SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                    .withTableName("users")
                    .usingGeneratedKeyColumns("id_user");

            Map<String, Object> values = new HashMap<>();
            values.put("email", user.getEmail());
            values.put("login", user.getLogin());
            values.put("name", user.getName());
            values.put("birthday", user.getBirthday());
            user.setId(insert.executeAndReturnKey(values).intValue());

            return user;
        } else {
            throw new UserCreatePreviouslyException(
                    MessageFormat.format("Пользователь с id: {0} уже зарегистрирован", user.getId()));
        }
    }

    @Override
    public User updateUser(User user) {
        if (findUserById(user.getId()) != null) {
            String sqlUpdate = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id_user = ?";
            jdbcTemplate.update(sqlUpdate,
                    user.getEmail(),
                    user.getLogin(),
                    user.getName(),
                    user.getBirthday(),
                    user.getId());
            return findUserById(user.getId());
        } else {
            throw new UserNotFountException(
                    MessageFormat.format("Пользователь с id: {0} не найден", user.getId()));
        }
    }

    @Override
    public int checkFriendshipStatus(int id, int idFriend) {
        String sql = "SELECT id_acceptance_status FROM users_friendship WHERE id_user_1 = ? AND id_user_2 = ?";
        SqlRowSet rows = jdbcTemplate.queryForRowSet(sql, id, idFriend);

        if (rows.next()) {
            return rows.getInt("id_acceptance_status");
        } else {
            return 1;
        }
    }

    @Override
    public void updateFriendship(int id, int idFriend, int idStatus) {
        String sql =
                "UPDATE users_friendship SET id_user_1 = ?, id_user_2 = ?, id_acceptance_status = ? " +
                        "WHERE id_user_1 = ? AND id_user_2 = ?";
        jdbcTemplate.update(sql, id, idFriend, idStatus, id, idFriend);
    }

    @Override
    public void insertFriendship(int id, int idFriend) {
        String sql = "INSERT INTO users_friendship (id_user_1, id_user_2, id_acceptance_status) VALUES(?, ?, ?)";
        jdbcTemplate.update(sql, id, idFriend, 1);
    }

    @Override
    public void removeFriendship(int id, int idFriend) {
        String sql = "DELETE FROM users_friendship WHERE id_user_1 = ? AND id_user_2 = ?";
        jdbcTemplate.update(sql, id, idFriend);
    }

    @Override
    public List<User> getFriendsUser(Integer idUser) {
        String sql = "SELECT * " +
                "FROM users_friendship AS f " +
                "INNER JOIN users AS u ON u.id_user = f.id_user_2 " +
                "WHERE f.id_user_1 = ? " +
                "ORDER BY u.id_user";
        return jdbcTemplate.query(sql, this::makeUser, idUser);
    }

    @Override
    public List<User> findMutualFriends(Integer userId, Integer otherUserId) {
        String sql = "SELECT * " +
                "FROM users_friendship AS f " +
                "INNER JOIN users_friendship fr ON fr.id_user_2 = f.id_user_2 " +
                "INNER JOIN users u ON u.id_user = fr.id_user_2 " +
                "WHERE f.id_user_1 = ? AND fr.id_user_1 = ? " +
                "AND f.id_user_2 <> fr.id_user_1 AND fr.id_user_2 <> f.id_user_1";
        return jdbcTemplate.query(sql, this::makeUser, userId, otherUserId);
    }

    private void loadFriends(User user) {
        String sql = "SELECT id_user_2 FROM users_friendship WHERE id_user_1 = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, user.getId());
        while (sqlRowSet.next()) {
            User friend = findUserById(sqlRowSet.getInt("id_user_2"));
            if (friend != null) {
                user.setFriend(friend);
            }
        }

        sql = "SELECT id_user_1 FROM users_friendship WHERE id_user_2 = ? AND id_acceptance_status = 2";
        sqlRowSet = jdbcTemplate.queryForRowSet(sql, user.getId());
        while (sqlRowSet.next()) {
            User friend = findUserById(sqlRowSet.getInt("id_user_1"));
            if (friend != null) {
                user.setFriend(friend);
            }
        }
    }

    private User makeUser(ResultSet rs, int rowNum) throws SQLException {
        User user = new User(
                rs.getInt("id_user"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getString("birthday")
        );
        loadFriends(user);
        return user;
    }
}
