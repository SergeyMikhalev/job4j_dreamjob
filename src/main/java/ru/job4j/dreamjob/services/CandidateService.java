package ru.job4j.dreamjob.services;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.CandidateStore;

import java.util.Collection;

@ThreadSafe
@Service
public class CandidateService {
    private final CandidateStore candidateStore;
    private final CityService cityService;

    public CandidateService(CandidateStore candidateStore, CityService cityService) {
        this.candidateStore = candidateStore;
        this.cityService = cityService;
    }

    public Collection<Candidate> findAll() {
        return candidateStore.findAll();
    }

    public Candidate findById(int id) {
        return candidateStore.findById(id);
    }

    public void add(Candidate candidate) {
        updateCityInCandidate(candidate);
        candidateStore.add(candidate);
    }

    public void update(Candidate candidate) {
        updateCityInCandidate(candidate);
        candidateStore.update(candidate);
    }

    private void updateCityInCandidate(Candidate candidate) {
        candidate.setCity(cityService.findOrDefault(candidate.getCity().getId()));
    }
}
