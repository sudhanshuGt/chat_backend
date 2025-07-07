package com.messanger.app.messanger.entity;

 
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AppUser sender;

    @ManyToOne
    private AppUser receiver;

    private String content;

    private Date timestamp = new Date();

    public Message() {}

    public Message(AppUser sender, AppUser receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = new Date();
    }

 }


