package com.messanger.app.messanger.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
