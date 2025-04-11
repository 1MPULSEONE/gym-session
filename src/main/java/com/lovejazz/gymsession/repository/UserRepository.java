package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.role.Role;
import com.lovejazz.gymsession.model.user.SignInDTO;
import com.lovejazz.gymsession.model.user.User;
import com.lovejazz.gymsession.model.user.UserDTO;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.UUID;

import org.springframework.util.Assert;

@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;
    private static final String DEFAULT_USER_ROLE = "client_user";

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<User> findByUserName(String username) {
        
        String query = "SELECT ud.id, ud.username, r.id as role_id, r.name as role_name FROM user_data ud JOIN user_roles ur ON ud.id = ur.user_id JOIN role r ON ur.role_id = r.id WHERE ud.username = :username";
        List<UserDTO> userDTOS = jdbcClient.sql(query)
                .param("username", username)
                .query((rs, rowNum) -> {
                    Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                    UUID userId = rs.getObject("id", UUID.class);
                    return new UserDTO(
                            userId,
                            rs.getString("username"),
                            role);
                }).list();
        if (userDTOS.isEmpty()) {
            return Optional.empty();
        }
        Map<UUID, List<UserDTO>> userDTOMap = new HashMap<>();
        for (UserDTO userDTO : userDTOS) {
            userDTOMap.computeIfAbsent(userDTO.id(), k -> new ArrayList<>()).add(userDTO);
        }

        if (userDTOMap.size() > 1) {
            throw new IllegalStateException("Multiple users found with username: " + username);
        }

        Map.Entry<UUID, List<UserDTO>> entry = userDTOMap.entrySet().iterator().next();
        List<UserDTO> userDTOList = entry.getValue();

        UserDTO firstUserDTO = userDTOList.getFirst();

        List<Role> roles = new ArrayList<>();
        for (UserDTO userDTO : userDTOList) {
            roles.add(userDTO.role());
        }

        return Optional.of(new User(
                firstUserDTO.id(),
                firstUserDTO.username(),
                roles));
    }

    public void createUser(SignInDTO dto) {

        System.out.println("Create user");

        UUID userId = UUID.randomUUID();

        var createdUserData = jdbcClient.sql("INSERT INTO user_data(id,username,email,first_name,last_name) values(?,?,?,?,?)")
                .params(List.of(
                        userId,
                        dto.username(),
                        dto.email(),
                        dto.firstName(),
                        dto.lastName()
                ))
                .update();

        Assert.state(createdUserData == 1, "Не удалось создать пользователя " + dto.username());

        Integer clientUserRoleId = jdbcClient.sql("SELECT id FROM role WHERE name = :roleName")
                .param("roleName", DEFAULT_USER_ROLE)
                .query(Integer.class)
                .optional()
                .orElseThrow(() -> new IllegalStateException("Роль '" + DEFAULT_USER_ROLE + "' не найдена в базе данных"));
        System.out.println(clientUserRoleId);

        var createdUserRole = jdbcClient.sql("INSERT INTO user_roles(user_id, role_id) VALUES (?, ?)")
                .params(List.of(userId, clientUserRoleId))
                .update();

        Assert.state(createdUserRole == 1, "Не удалось назначить роль '" + DEFAULT_USER_ROLE + "' пользователю " + dto.username());
    }



//    public Optional<User> findByUserName(String username) {   
//        String query = """
//                SELECT ud.id, ud.login, ud.password, r.id as role_id, r.name as role_name
//                FROM user_data ud
//                JOIN user_roles ur ON ud.id = ur.user_id
//                JOIN role r ON ur.role_id = r.id
//                WHERE ud.username = ?
//                """;
//        List<HashMap> result = jdbcClient.sql(query)
//                .params(List.of(username))
//                .query(HashMap.class)
//                .list();
//        if (result.isEmpty()) {
//            return Optional.empty();
//        }
//
//
//        Integer userId = null;
//        String userLogin = null;
//        String userPassword = null;
//        List<Role> roles = new ArrayList<>();
//
//        for (Map row : result) {
//            if (userId == null) {
//                userId = (Integer) row.get("id");
//                userLogin = (String) row.get("login");
//                userPassword = (String) row.get("password");
//            }
//            int roleId = (Integer) row.get("role_id");
//            String roleName = (String) row.get("role_name");
//            roles.add(new Role(roleId, roleName));
//        }
//        return Optional.of(new User(userId, userPassword, userLogin, roles));
//    }


}
