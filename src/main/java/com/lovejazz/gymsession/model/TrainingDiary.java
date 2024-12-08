package com.lovejazz.gymsession.model;


import jakarta.validation.constraints.NotEmpty;

public record TrainingDiary(
        Integer id,
        Integer userId,
        @NotEmpty
        String name,
        @NotEmpty
        SportType sportType
) {
}