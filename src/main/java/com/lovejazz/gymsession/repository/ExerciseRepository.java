package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.exercise.ExerciseDto;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

@Repository
public class ExerciseRepository {
    private final JdbcClient jdbcClient;

    public ExerciseRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public void upsertExercise(ExerciseDto exercise) {
        boolean exists = jdbcClient.sql("""
                SELECT COUNT(*) > 0 
                FROM exercises 
                WHERE name = ? AND muscle = ? AND equipment = ?
                """)
                .params(exercise.getName(), exercise.getMuscle(), exercise.getEquipment())
                .query(Boolean.class)
                .single();

        if (exists) {

            int updated = jdbcClient.sql("""
                    UPDATE exercises 
                    SET type = ?, difficulty = ?, instructions = ?
                    WHERE name = ? AND muscle = ? AND equipment = ?
                    """)
                    .params(
                            exercise.getType(),
                            exercise.getDifficulty(),
                            exercise.getInstructions(),
                            exercise.getName(),
                            exercise.getMuscle(),
                            exercise.getEquipment()
                    )
                    .update();

            Assert.state(updated == 1, "Failed to update exercise " + exercise.getName());
        } else {

            UUID id = UUID.randomUUID();
            int created = jdbcClient.sql("""
                    INSERT INTO exercises(id, name, type, muscle, equipment, difficulty, instructions) 
                    VALUES (?, ?, ?, ?, ?, ?, ?)
                    """)
                    .params(
                            id,
                            exercise.getName(),
                            exercise.getType(),
                            exercise.getMuscle(),
                            exercise.getEquipment(),
                            exercise.getDifficulty(),
                            exercise.getInstructions()
                    )
                    .update();

            Assert.state(created == 1, "Failed to create exercise " + exercise.getName());
        }
    }
}
