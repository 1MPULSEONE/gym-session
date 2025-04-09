package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public class TrainingDiaryRepository {
    private final JdbcClient jdbcClient;

    public TrainingDiaryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<TrainingDiaryDTO> findAll() {
        return jdbcClient.sql("SELECT * FROM Training_Diary")
                .query((rs, rowNum) -> {
                    UUID userId = rs.getObject("user_id", UUID.class);
                    return new TrainingDiaryDTO(rs.getInt("id"),
                            userId,
                            rs.getString("name"),
                            rs.getInt("sport_type_id"));
                })
                .list();
    }

    public Optional<TrainingDiaryDTO> findById(@RequestParam Integer id, UUID userId) {
        return jdbcClient.sql("SELECT * FROM Training_Diary WHERE id = :id AND user_id = :userId")
                .param("id", id)
                .param("userId", userId)
                .query((rs, rowNum) -> {
                    UUID trainingDiaryUserId = rs.getObject("user_id", UUID.class);
                    return new TrainingDiaryDTO(rs.getInt("id"),
                            trainingDiaryUserId,
                            rs.getString("name"),
                            rs.getInt("sport_type_id"));
                })
                .optional();
    }


    public void create(TrainingDiaryDTO diary, UUID userId) {
        var created = jdbcClient.sql("INSERT INTO Training_Diary(id,user_id,name,sport_type_id) values(?,?,?,?)")
                .params(List.of(diary.id(), userId, diary.name(), diary.sportTypeId()))
                .update();
        Assert.state(created == 1, "Failed to create run " + diary.name());

    }

    public void update(TrainingDiaryDTO diary, Integer diaryId, UUID userId) {
        var updated = jdbcClient.sql("UPDATE Training_Diary set id = ?, user_id = ?, name = ?, sport_type_id = ? where id = ?")
                .params(List.of(diary.id(), userId, diary.name(), diary.sportTypeId(), diaryId))
                .update();
        Assert.state(updated == 1, "Failed to update run " + diary.name());
    }

    public void delete(Integer id, UUID userId) {
        var deleted = jdbcClient.sql("DELETE from Training_Diary WHERE id = :id and user_id = :userId").param("id", id).param("user_id", userId).update();
    }

}
