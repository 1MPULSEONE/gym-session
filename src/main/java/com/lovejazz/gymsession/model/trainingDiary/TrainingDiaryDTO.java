package com.lovejazz.gymsession.model.trainingDiary;

import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record TrainingDiaryDTO(
        Integer id,
        UUID userId,
        @NotEmpty
        String name,
        Integer sportTypeId
) {
}