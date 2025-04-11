package com.lovejazz.gymsession.model.trainingDiary;

import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import jakarta.validation.constraints.NotEmpty;

import java.util.UUID;

public record TrainingDiaryDAO(
        Integer id,
        UUID userId,
        @NotEmpty
        String name,
        SportTypeDAO sportType
) {
}