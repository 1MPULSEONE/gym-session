package com.lovejazz.gymsession.model.user;

import com.lovejazz.gymsession.model.role.Role;

public record UserDTO(
        Integer id,
        String password,
        String username,
        Role role
) {

}