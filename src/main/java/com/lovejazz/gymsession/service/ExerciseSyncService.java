package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.exercise.ExerciseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ExerciseSyncService {
    private final WebClient webClient;
    private final ExerciseService exerciseService;

    public  ExerciseSyncService(WebClient.Builder webClientBuilder, ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
        this.webClient = webClientBuilder
                .baseUrl("http://localhost:7070/api/")
                .build();

    }

    public List<ExerciseDto> getExercisesByMuscle(String muscle) {
        List<ExerciseDto> exerciseDtos =  webClient.get()
                .uri("/exercises?muscle={muscle}", muscle)
                .retrieve()
                .bodyToFlux(ExerciseDto.class)
                .collectList()
                .block();

        exerciseService.processExercises(exerciseDtos);
        return exerciseDtos;
    }
}