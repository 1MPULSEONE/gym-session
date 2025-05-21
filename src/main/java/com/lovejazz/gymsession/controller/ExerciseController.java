package com.lovejazz.gymsession.controller;

import com.lovejazz.gymsession.model.exercise.ExerciseDto;
import com.lovejazz.gymsession.service.ExerciseFacade;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
public class ExerciseController {
    private final ExerciseFacade exerciseFacade;

    public ExerciseController(ExerciseFacade exerciseFacade) {
        this.exerciseFacade = exerciseFacade;
    }

    @GetMapping
    public List<ExerciseDto> getExercisesByMuscle(@RequestParam String muscle) {
        return exerciseFacade.getExercises(muscle);
    }
}