package ru.job4j.dreamjob.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
public class CandidateDBStore {

    public static final Logger LOG = LoggerFactory.getLogger(PostDBStore.class.getName());
    private final BasicDataSource pool;

    public CandidateDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public Collection<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM candidate")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next(candidates.add(
                        new Candidate(it.getInt("id"),
                                it.getString("name"),
                                it.getString("surname"),
                                it.getString("description"),
                                it.getTimestamp("registered").toLocalDateTime().toLocalDate(),
                                null,//it.getInt("city_id"),
                                null //it.getArray("photo")
                        )
                )));
            }

        } catch (Exception e) {
            LOG.error("Exception : ", e);
        }

        return null;
    }

    public Candidate findById(int id) {
        return null;
    }

    public void add(Candidate candidate) {

    }

    public void update(Candidate candidate) {
    }
}
