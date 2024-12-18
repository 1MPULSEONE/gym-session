package com.lovejazz.gymsession.repository;

import com.lovejazz.gymsession.model.role.Role;
import com.lovejazz.gymsession.model.user.User;
import com.lovejazz.gymsession.model.user.UserDTO;
import com.lovejazz.gymsession.utils.exceptions.RunNotFoundExceptions;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserRepository {
    private final JdbcClient jdbcClient;

    public UserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<User> findByUserName(String username) {
       //usrname = dima
        String query = "SELECT ud.id, ud.username, ud.password, r.id as role_id, r.name as role_name FROM user_data ud JOIN user_roles ur ON ud.id = ur.user_id JOIN role r ON ur.role_id = r.id WHERE ud.username = :username"
                ;
        List<UserDTO> userDTOS = jdbcClient.sql(query)
                .param("username", username)
                .query((rs, rowNum) -> {
                    Role role = new Role(rs.getInt("role_id"), rs.getString("role_name"));
                    return new UserDTO(rs.getInt("id"),
                            rs.getString("password"),
                            rs.getString("username"),
                            role);
                }).list();
//        if(true) {
//            throw new IllegalStateException("userDTOS" + userDTOS);
//        }
        if (userDTOS.isEmpty()) {
            return Optional.empty();
        }
        //dima admin , dima admin -> User(dima , [admin,admin])
        Map<Integer, List<UserDTO>> userDTOMap = new HashMap<>();
        for (UserDTO userDTO : userDTOS) {
            userDTOMap.computeIfAbsent(userDTO.id(), k -> new ArrayList<>()).add(userDTO);
        }

        if (userDTOMap.size() > 1) {
            throw new IllegalStateException("Multiple users found with username: " + username);
        }

        Map.Entry<Integer, List<UserDTO>> entry = userDTOMap.entrySet().iterator().next();
        List<UserDTO> userDTOList = entry.getValue();

        // Берем данные первого UserDTO для полей id, password, username (они одинаковые для всех)
        UserDTO firstUserDTO = userDTOList.getFirst();

        // Собираем список ролей
        List<Role> roles = new ArrayList<>();
        for (UserDTO userDTO : userDTOList) {
            roles.add(userDTO.role());
        }

        // Создаем и возвращаем User
        return Optional.of(new User(firstUserDTO.id(), firstUserDTO.password(), firstUserDTO.username(), roles));
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
