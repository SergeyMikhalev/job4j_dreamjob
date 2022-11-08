package ru.job4j.dreamjob.store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.job4j.dreamjob.Main;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.City;

import java.sql.Connection;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class CandidateDBStoreTest {

    @BeforeEach
    public void prepareDb() {
        try (Connection cn = (new Main().loadPool()).getConnection();
             Statement st = cn.createStatement()) {
            st.execute("DELETE FROM candidate");
        } catch (Exception e) {
        }
    }

    @Test
    public void whenCreateCandidate() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(
                100,
                "Petr",
                "Petrov",
                "Developer",
                LocalDate.now(),
                new City(1, "Msk"),
                null);
        candidate.setId(store.add(candidate).getId());
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
        Assertions.assertEquals(candidateInDb, candidate);
    }

    @Test
    public void whenCreateTwoCandidates() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate1 = new Candidate(
                100,
                "Petr",
                "Petrov",
                "Developer",
                LocalDate.now(),
                new City(1, "Msk"),
                null);
        Candidate candidate2 = new Candidate(
                200,
                "Nikolay",
                "Egorov",
                "Senior Developer",
                LocalDate.now(),
                new City(1, "Ekb"),
                null);

        candidate1.setId(store.add(candidate1).getId());
        candidate2.setId(store.add(candidate2).getId());
        List<Candidate> candidatesInDb = store.findAll();
        List<Candidate> candidates = List.of(candidate1, candidate2);

        assertTrue(candidatesInDb.containsAll(candidates));
        assertEquals(2, candidatesInDb.size());
    }

    @Test
    public void whenUpdatePost() {
        CandidateDBStore store = new CandidateDBStore(new Main().loadPool());
        Candidate candidate = new Candidate(
                100,
                "Petr",
                "Petrov",
                "Developer",
                LocalDate.now(),
                new City(1, "Msk"),
                null);
        candidate.setId(store.add(candidate).getId());
        candidate.setDescription("New description now!");
        store.update(candidate);
        Candidate candidateInDb = store.findById(candidate.getId());
        assertThat(candidateInDb.getName(), is(candidate.getName()));
        Assertions.assertEquals(candidateInDb, candidate);
    }


}