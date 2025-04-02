package com.lovejazz.gymsession.payload.request;

import jakarta.validation.constraints.NotBlank;

public class LoginViaGoogleRequest {
    @NotBlank
    private String googleAccessToken;

    public  String getGoogleAccessToken(){return  this.googleAccessToken;}
}
