package com.messanger.app.messanger.dto;

import com.messanger.app.messanger.entity.AppUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String bio;

    public UserSearchResponse(AppUser user) {
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.bio = user.getBio();
    }
 
}
