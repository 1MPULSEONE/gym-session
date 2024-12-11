package com.lovejazz.gymsession.model.trainingDiary;

import com.lovejazz.gymsession.model.sportType.SportTypeDAO;
import jakarta.validation.constraints.NotEmpty;

public record TrainingDiaryDAO(
        Integer id,
        Integer userId,
        @NotEmpty
        String name,
        SportTypeDAO sportType
) {
}