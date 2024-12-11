package com.lovejazz.gymsession.model.trainingDiary;

import jakarta.validation.constraints.NotEmpty;

public record TrainingDiaryDTO(
        Integer id,
        Integer userId,
        @NotEmpty
        String name,
        Integer sportTypeId
) {
}