package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.trainingDiary.TrainingDiaryDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Repository
public class TrainingDiaryRepository {
    private final JdbcClient jdbcClient;

    public TrainingDiaryRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<TrainingDiaryDTO> findAll() {
        return jdbcClient.sql("SELECT * FROM Training_Diary")
                .query((rs, rowNum) -> {;
                    return new TrainingDiaryDTO(rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getInt("sport_type_id"));
                })
                .list();
    }

    public Optional<TrainingDiaryDTO> findById(@RequestParam Integer id) {
        return jdbcClient.sql("SELECT * FROM Training_Diary WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> new TrainingDiaryDTO(rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("name"),
                        rs.getInt("sport_type_id")))
                .optional();
    }


    public void create(TrainingDiaryDTO diary) {
        var created = jdbcClient.sql("INSERT INTO Training_Diary(id,user_id,name,sport_type_id) values(?,?,?,?)")
                .params(List.of(diary.id(), 1, diary.name(), diary.sportTypeId()))
                .update();
        Assert.state(created == 1, "Failed to create run " + diary.name());

    }

    public void update(TrainingDiaryDTO diary, Integer id) {
        var updated = jdbcClient.sql("UPDATE Training_Diary set id = ?, user_id = ?, name = ?, sport_type_id = ? where id = ?")
                .params(List.of(diary.id(), 1, diary.name(), diary.sportTypeId(), id))
                .update();
        Assert.state(updated == 1, "Failed to update run " + diary.name());
    }

    public void delete(Integer id) {
        var deleted = jdbcClient.sql("DELETE from Training_Diary WHERE id = :id").param("id", id).update();
    }

}
