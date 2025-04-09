package com.lovejazz.gymsession.model.user;

import java.util.UUID;

import com.lovejazz.gymsession.model.role.Role;

public record UserDTO(
        UUID id,
        String username,
        Role role
) {

}