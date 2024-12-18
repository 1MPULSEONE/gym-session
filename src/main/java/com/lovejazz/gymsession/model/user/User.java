package com.lovejazz.gymsession.model.user;

import com.lovejazz.gymsession.model.role.Role;

import java.util.List;

public record User(
        Integer id,
        String password,
        String username,
        List<Role> roles
) {

}

