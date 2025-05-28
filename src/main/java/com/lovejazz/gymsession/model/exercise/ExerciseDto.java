package com.lovejazz.gymsession.model.exercise;
import lombok.Data;

@Data
public class ExerciseDto {
    private String name;
    private String type;
    private String muscle;
    private String equipment;
    private String difficulty;
    private String instructions;
}
