package com.messanger.app.messanger.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    private String profile;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String firstName;
    private String lastName;
    private String bio;

    @JsonIgnore
    private String password;
    @JsonIgnore
    private String role = "ROLE_USER";  
 
}
