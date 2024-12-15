package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDAO;
import com.lovejazz.gymsession.service.TrainingDiaryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    List<TrainingDiaryDAO> findAll() {
        return trainingDiaryService.findAll();
    }

    @GetMapping("/{id}")
    TrainingDiaryDAO findById(@PathVariable Integer id) {
        TrainingDiaryDAO trainingDiary = trainingDiaryService.findById(id);
        return trainingDiary;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody TrainingDiaryDAO diary) {
        trainingDiaryService.create(diary);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody TrainingDiaryDAO diary, @PathVariable Integer id) {
        trainingDiaryService.update(diary, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        trainingDiaryService.delete(id);
    }

}
