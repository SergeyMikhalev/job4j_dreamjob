package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDBStore {
    public static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());
    public static final String SELECT_ALL = "SELECT * FROM candidate";
    public static final String SELECT_BY_ID = "SELECT * FROM candidate WHERE id = ?";
    public static final String INSERT =
            "INSERT INTO candidate(name, surname, description, registered, city_id, photo) VALUES (?,?,?,?,?,?) ";
    public static final String UPDATE =
            "UPDATE candidate SET name=?, surname=?, description=?, registered=?, city_id=?, photo=? WHERE id=?";

    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_ALL)) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    candidates.add(getCandidate(it));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }
        return candidates;
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return getCandidate(it);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }
        return null;
    }

    public Candidate add(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(INSERT,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getSurname());
            ps.setString(3, candidate.getDescription());
            ps.setTimestamp(4,
                    Timestamp.valueOf(LocalDateTime.of(candidate.getRegistered(), LocalTime.MIN)));
            ps.setInt(5, candidate.getCity().getId());
            ps.setBytes(6, candidate.getPhoto());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }
        return candidate;
    }

    public void update(Candidate candidate) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(UPDATE)
        ) {
            ps.setString(1, candidate.getName());
            ps.setString(2, candidate.getSurname());
            ps.setString(3, candidate.getDescription());
            ps.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            ps.setInt(5, candidate.getCity().getId());
            ps.setBytes(6, candidate.getPhoto());
            ps.setInt(7, candidate.getId());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }
    }

    private Candidate getCandidate(ResultSet resultSet) throws SQLException {
        return new Candidate(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("description"),
                resultSet.getTimestamp("registered").toLocalDateTime().toLocalDate(),
                new City(resultSet.getInt("city_id"), ""),
                resultSet.getBytes("photo")
        );
    }
}
