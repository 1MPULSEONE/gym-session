package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.exercise.ExerciseDto;

import java.util.List;

public interface ExerciseService {
    List<ExerciseDto> getExercisesByMuscle(String muscle);
}
