package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UsersDBStore {

    public static final Logger LOG = LoggerFactory.getLogger(UsersDBStore.class.getName());
    private final BasicDataSource pool;

    public static final String INSERT =
            "INSERT INTO users(name,email,password) VALUES (?,?,?)";

    public static final String FIND_BY_EMAIL_AND_PASS =
            "SELECT * FROM users WHERE email = ? and password =?";

    public UsersDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT,
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                    result = Optional.of(user);
                }
            }
        } catch (SQLException e) {
            LOG.error("Error: ", e);
        }
        return result;
    }

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        Optional<User> result = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_EMAIL_AND_PASS)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    result = Optional.of(
                            new User(
                                    rs.getInt("id"),
                                    rs.getString("name"),
                                    rs.getString("email"),
                                    rs.getString("password")
                            )
                    );
                }
            }
        } catch (SQLException e) {
            LOG.error("Error: ", e);
        }
        return result;
    }
}
