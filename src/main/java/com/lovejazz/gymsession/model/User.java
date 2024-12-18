package com.lovejazz.gymsession.model;

import jakarta.validation.constraints.NotEmpty;

public record User(
        @NotEmpty
        Integer id,
        @NotEmpty
        String phoneNumber,
        @NotEmpty
        String password
) {

}
