package ru.job4j.dreamjob.store;

import org.springframework.stereotype.Repository;
import ru.job4j.dreamjob.model.Candidate;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class CandidateStore {
    private final Map<Integer, Candidate> candidates = new ConcurrentHashMap<>();
    private final AtomicInteger newId = new AtomicInteger(4);

    public CandidateStore() {
        candidates.put(1,
                new Candidate(1,
                        "Николай",
                        "Плюхин",
                        "Python Middle Developer",
                        LocalDate.now())
        );
        candidates.put(2,
                new Candidate(2,
                        "Сергей",
                        "Кожевников",
                        "Python Middle Developer",
                        LocalDate.now())
        );
        candidates.put(3,
                new Candidate(3,
                        "Василий",
                        "Разорвинога",
                        "Solution Architect",
                        LocalDate.now())
        );
    }

    public Collection<Candidate> findAll() {
        return candidates.values();
    }

    public Candidate findById(int id) {
        return candidates.get(id);
    }

    public void add(Candidate candidate) {
        int id = newId.getAndIncrement();
        candidate.setId(id);
        candidates.put(id, candidate);
    }

    public void update(Candidate candidate) {
        candidates.replace(candidate.getId(), candidate);
    }

}
