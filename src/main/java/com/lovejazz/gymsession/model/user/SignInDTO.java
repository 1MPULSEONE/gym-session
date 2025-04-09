package com.lovejazz.gymsession.model.user;

public record SignInDTO(
        String email,
        String username,
        String firstName,
        String lastName
) {

}
