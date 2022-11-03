package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.service.CityService;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class CandidateDBStore {

    public static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());
    public static final String SELECT_ALL = "SELECT * FROM candidate";
    public static final String SELECT_BY_ID = "SELECT * FROM candidate WHERE id = ?";

    private final BasicDataSource pool;
    private final CityService cityService;

    public CandidateDBStore(BasicDataSource pool, CityService cityService) {
        this.pool = pool;
        this.cityService = cityService;
    }

    public Collection<Candidate> findAll() {
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
        return null;
    }

    public Candidate findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(SELECT_BY_ID)) {
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

    public void add(Candidate candidate) {

    }

    public void update(Candidate candidate) {
    }

    private Candidate getCandidate(ResultSet resultSet) throws SQLException {
        return new Candidate(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("description"),
                resultSet.getTimestamp("registered").toLocalDateTime().toLocalDate(),
                cityService.findById(resultSet.getInt("city_id")),
                resultSet.getBytes("photo")
        );
    }
}
