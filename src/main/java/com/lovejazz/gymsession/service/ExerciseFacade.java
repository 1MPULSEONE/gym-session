package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.exercise.ExerciseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExerciseFacade {
    private final ExerciseService exerciseService;

    public ExerciseFacade(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    public List<ExerciseDto> getExercises(String muscle) {
        return exerciseService.getExercisesByMuscle(muscle);
    }
}