package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDAO;
import com.lovejazz.gymsession.service.TrainingDiaryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainingDiary")
public class TrainingDiaryController {

    private final TrainingDiaryService trainingDiaryService;

    public TrainingDiaryController(TrainingDiaryService service) {
        this.trainingDiaryService = service;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('ROLE_ROLE_ADMIN') ")
    List<TrainingDiaryDAO> findAll() {
        return trainingDiaryService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    TrainingDiaryDAO findById(@PathVariable Integer id) {
        return trainingDiaryService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    @PreAuthorize("hasRole('USER')")
    void create(@Valid @RequestBody TrainingDiaryDAO diary) {
        trainingDiaryService.create(diary);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    void update(@Valid @RequestBody TrainingDiaryDAO diary, @PathVariable Integer id) {
        trainingDiaryService.update(diary, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    void delete(@PathVariable Integer id) {
        trainingDiaryService.delete(id);
    }

}
