package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.model.TrainingDiary;
import com.lovejazz.gymsession.repository.TrainingDiaryRepository;
import com.lovejazz.gymsession.utils.exceptions.RunNotFoundExceptions;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trainingDiary")
public class TrainingDiaryController {

    private final TrainingDiaryRepository trainingDiaryRepository;

    public TrainingDiaryController(TrainingDiaryRepository repository) {
        this.trainingDiaryRepository = repository;
    }

    @GetMapping("")
    List<TrainingDiary> findAll() {
        return trainingDiaryRepository.findAll();
    }

    @GetMapping("/{id}")
    TrainingDiary findById(@PathVariable Integer id) {
        Optional<TrainingDiary> trainingDiary = trainingDiaryRepository.findById(id);
        if (trainingDiary.isEmpty()) {
            throw new RunNotFoundExceptions();
        }
        return trainingDiary.get();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    void create(@Valid @RequestBody TrainingDiary diary) {
        trainingDiaryRepository.create(diary);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    void update(@Valid @RequestBody TrainingDiary diary, @PathVariable Integer id) {
        trainingDiaryRepository.update(diary, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    void delete(@PathVariable Integer id) {
        trainingDiaryRepository.delete(id);
    }

}
