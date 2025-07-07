package com.messanger.app.messanger.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor 
public class MessageDTO {
    private UserDTO sender;
    private UserDTO receiver;
    private String content;
    private LocalDateTime timestamp;
}

