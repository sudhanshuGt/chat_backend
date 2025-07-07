package com.messanger.app.messanger.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String password;
}
