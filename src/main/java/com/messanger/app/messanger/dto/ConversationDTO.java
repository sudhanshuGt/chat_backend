package com.messanger.app.messanger.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data; 


@Data 
@AllArgsConstructor
public class ConversationDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String lastMessage;
    private LocalDateTime timestamp;


 }