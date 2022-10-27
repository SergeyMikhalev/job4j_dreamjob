package ru.job4j.dreamjob.controller;


import net.jcip.annotations.ThreadSafe;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.job4j.dreamjob.model.Candidate;
import ru.job4j.dreamjob.services.CandidateService;
import ru.job4j.dreamjob.services.CityService;

import java.io.IOException;
import java.time.LocalDate;

@ThreadSafe
@Controller
public class CandidateController {

    private final CandidateService candidateService;
    private final CityService cityService;

    public CandidateController(CandidateService candidateService, CityService cityService) {
        this.candidateService = candidateService;
        this.cityService = cityService;
    }

    @GetMapping("/candidates")
    public String candidates(Model model) {
        model.addAttribute("candidates", candidateService.findAll());
        return "candidates";
    }

    @GetMapping("/formAddCandidate")
    public String addCandidate(Model model) {
        model.addAttribute("cities", cityService.getAllCities());
        model.addAttribute("candidate",
                new Candidate(0,
                        "Заполните поле",
                        "Заполните поле",
                        "Заполните поле",
                        LocalDate.now(),
                        cityService.getDefault(),
                        null));
        return "addCandidate";
    }

    @PostMapping("createCandidate")
    public String createCandidate(@RequestParam("file") MultipartFile file,
                                  @ModelAttribute Candidate candidate) throws IOException {
        candidate.setPhoto(file.getBytes());
        candidateService.add(candidate);
        System.out.println(file.getName());
        return "redirect:/candidates";
    }

    @GetMapping("/formUpdateCandidate/{candidateId}")
    public String formUpdatePost(Model model, @PathVariable("candidateId") int id) {
        model.addAttribute("candidate", candidateService.findById(id));
        model.addAttribute("cities", cityService.getAllCities());
        return "updateCandidate";
    }

    @PostMapping("updateCandidate")
    public String updateCandidate(@RequestParam("file") MultipartFile file,
                                  @ModelAttribute Candidate candidate) throws IOException {
        candidate.setPhoto(file.getBytes());
        candidateService.update(candidate);
        return "redirect:/candidates";
    }

    @GetMapping("/photoCandidate/{candidateId}")
    public ResponseEntity<Resource> download(@PathVariable("candidateId") Integer candidateId) {
        Candidate candidate = candidateService.findById(candidateId);
        return ResponseEntity.ok()
                .headers(new HttpHeaders())
                .contentLength(candidate.getPhoto().length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new ByteArrayResource(candidate.getPhoto()));
    }
}
