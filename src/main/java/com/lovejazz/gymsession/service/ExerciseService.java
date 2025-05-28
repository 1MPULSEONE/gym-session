package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.exercise.ExerciseDto;
import com.lovejazz.gymsession.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExerciseService {
    private final ExerciseRepository exerciseRepository;

    @Transactional
    public void processExercises(List<ExerciseDto> exercises) {
        exercises.forEach(exercise -> {
            exerciseRepository.upsertExercise(exercise);
        });
    }
}