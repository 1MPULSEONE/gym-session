package com.lovejazz.gymsession.payload.request;

import jakarta.validation.constraints.NotBlank;

public class SignUpRequest {
    @NotBlank
    private  String username;

    @NotBlank
    private  String email;

    @NotBlank
    private  String firstName;

    @NotBlank
    private  String lastName;

    @NotBlank
    private  String password;

    public String getUsername() {return this.username;}

    public String getEmail() {return this.email;}

    public String getFirstName() {return this.firstName;}

    public String getLastName() {return this.lastName;}

    public String getPassword() {return this.password;}


}
