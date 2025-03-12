package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.role.Role;
import com.lovejazz.gymsession.model.user.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Repository
public class RoleRepository {
    private final JdbcClient jdbcClient;

    public RoleRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Role> findAll() {
        return jdbcClient.sql("SELECT * FROM Role")
                .query((rs, rowNum) -> new Role(rs.getInt("id"),
                        rs.getString("name")
                )).list();
    }

    public Optional<Role> findById(@RequestParam Integer id) {
        return jdbcClient.sql("SELECT * FROM Role WHERE id = :id")
                .param("id", id)
                .query((rs, rowNum) -> {
                    return new Role(rs.getInt("id"),
                            rs.getString("name"));
                })
                .optional();
    }

    public void create(Role role) {
        var created = jdbcClient.sql("INSERT INTO Role(id,name) values(?,?)")
                .params(List.of(role.id(), role.name()))
                .update();
        Assert.state(created == 1, "Failed to create run " + role.name());
    }

    public void update(Role role,Integer id) {
        var updated = jdbcClient.sql("UPDATE Role set id= ?, name = ? where id = ?")
                .params(List.of(role.id(), role.name(), id))
                .update();
        Assert.state(updated == 1, "Failed to update run " + role.name());
    }

    public void delete(Integer id) {
        var deleted = jdbcClient.sql("DELETE from Role WHERE id = :id").param("id", id).update();
    }

}
