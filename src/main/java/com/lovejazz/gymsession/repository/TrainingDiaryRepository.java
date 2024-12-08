package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.SportType;
import com.lovejazz.gymsession.model.TrainingDiary;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
public class TrainingDiaryRepository {
    private final List<TrainingDiary> trainingDiaryList = new ArrayList<>();
    private final JdbcClient jdbcClient;

    public TrainingDiaryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<TrainingDiary> findAll() {
        return jdbcClient.sql("SELECT * FROM TRAINING_DIARY")
                .query((rs, rowNum) -> {
                    Integer sportTypeId = rs.getInt("sport_type_id");
                    SportType sportType = SportType.findById(sportTypeId);
                    return new TrainingDiary(rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            sportType);
                })
                .list();
    }

    public Optional<TrainingDiary> findById(Integer id) {
        return jdbcClient.sql("SELECT * FROM TRAINING_DIARY WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> {
                    Integer sportTypeId = rs.getInt("sport_type_id");
                    SportType sportType = SportType.findById(sportTypeId);
                    return new TrainingDiary(rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            sportType);
                })
                .optional();
    }


    public void create(TrainingDiary diary) {
        var updated = jdbcClient.sql("INSERT INTO TRAINING_DIARY(id,user_id,name,sport_type_id) values(?,?,?,?)")
                .params(List.of(diary.id(), 1, diary.name(), diary.sportType().getId()))
                .update();
        Assert.state(updated == 1, "Failed to update run " + diary.name());

    }

    public void update(TrainingDiary diary, Integer id) {
        var updated = jdbcClient.sql("UPDATE TRAINING_DIARY set id = ?, user_id = ?, name = ?, sport_type_id = ? where id = ?")
                .params(List.of(diary.id(), 1, diary.name(), diary.sportType().getId(), id))
                .update();
        Assert.state(updated == 1, "Failed to update run " + diary.name());
    }

    public void delete(Integer id) {
        var updated = jdbcClient.sql("DELETE from TRAINING_DIARY WHERE id = :id").param("id", id).update();
    }

    @PostConstruct
    private void init() {
        trainingDiaryList.add(new TrainingDiary(
                1, 1, "MyTrainingDiary1", SportType.GYM
        ));
        trainingDiaryList.add(new TrainingDiary(
                2, 1, "MyTrainingDiary2", SportType.POOL
        ));
    }
}
