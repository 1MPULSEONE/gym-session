package com.lovejazz.gymsession.model.user;

import com.lovejazz.gymsession.model.role.Role;

import java.util.List;
import java.util.UUID;

public record User(
        UUID id,
        String username,
        List<Role> roles
) {

}

