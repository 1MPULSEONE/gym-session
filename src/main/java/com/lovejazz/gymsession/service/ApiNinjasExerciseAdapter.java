package com.lovejazz.gymsession.service;

import com.lovejazz.gymsession.model.exercise.ExerciseDto;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class ApiNinjasExerciseAdapter implements ExerciseService {
    private final WebClient webClient;
    private final String apiKey;

    public ApiNinjasExerciseAdapter(WebClient.Builder webClientBuilder) {
        Dotenv dotenv = Dotenv.load(); 
        this.apiKey = dotenv.get("API_NINJAS_KEY");

        this.webClient = webClientBuilder
                .baseUrl("https://api.api-ninjas.com/v1")
                .defaultHeader("X-Api-Key", this.apiKey)
                .build();
    }

    @Override
    public List<ExerciseDto> getExercisesByMuscle(String muscle) {
        return webClient.get()
                .uri("/exercises?muscle={muscle}", muscle)
                .retrieve()
                .bodyToFlux(ExerciseDto.class)
                .collectList()
                .block();
    }
}