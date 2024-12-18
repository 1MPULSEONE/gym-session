package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.sportType.SportTypeDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public class SportTypeRepository {
    private final JdbcClient jdbcClient;

    public SportTypeRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<SportTypeDTO> findAll() {
        return jdbcClient.sql("SELECT * FROM SPORT_TYPE")
                .query((rs, rowNum) -> new SportTypeDTO(rs.getInt("id"),
                        rs.getString("title")
                ))
                .list();
    }

    public Optional<SportTypeDTO> findById(@RequestParam Integer id) {
        return jdbcClient.sql("SELECT * FROM Sport_Type WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> {
                    return new SportTypeDTO(rs.getInt("id"),
                            rs.getString("title"));
                })
                .optional();
    }

    public void create(SportTypeDTO sportTypeDTO) {
        var created = jdbcClient.sql("INSERT INTO Sport_Type(id,title) values(?,?)")
                .params(List.of(sportTypeDTO.id(), sportTypeDTO.title()))
                .update();
        Assert.state(created == 1, "Failed to create run " + sportTypeDTO.title());

    }

    public void update(SportTypeDTO sportTypeDTO, Integer id) {
        var updated = jdbcClient.sql("UPDATE Sport_Type set id= ?, title = ? where id = ?")
                .params(List.of(sportTypeDTO.id(), sportTypeDTO.title(), id))
                .update();
        Assert.state(updated == 1, "Failed to update run " + sportTypeDTO.title());
    }

    public void delete(Integer id) {
        var deleted = jdbcClient.sql("DELETE from Sport_Type WHERE id = :id").param("id", id).update();
    }

}
