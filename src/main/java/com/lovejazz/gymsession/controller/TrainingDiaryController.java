package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDAO;
import com.lovejazz.gymsession.service.TrainingDiaryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/trainingDiary")
public class TrainingDiaryController {

    private static final Logger logger = LoggerFactory.getLogger(TrainingDiaryController.class);

    private final TrainingDiaryService trainingDiaryService;

    public TrainingDiaryController(TrainingDiaryService service) {
        this.trainingDiaryService = service;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('client_admin') ")
    List<TrainingDiaryDAO> findAll() {
        return trainingDiaryService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('client_user')")
    TrainingDiaryDAO findById(@PathVariable Integer id) {
        return trainingDiaryService.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("")
    @PreAuthorize("hasRole('client_user')")
    void create(@Valid @RequestBody TrainingDiaryDAO diary,
                @CookieValue(name = "access_token", required = true) String accessToken) {

        logger.info("TEST CREATE CONTROLLER");

        logger.info("Создание TrainingDiary. Access Token из cookie: {}", accessToken);

        trainingDiaryService.create(diary, accessToken);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('client_user')")
    void update(@Valid @RequestBody TrainingDiaryDAO diary, @PathVariable Integer id) {
        trainingDiaryService.update(diary, id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_user')")
    void delete(@PathVariable Integer id) {
        trainingDiaryService.delete(id);
    }

}
