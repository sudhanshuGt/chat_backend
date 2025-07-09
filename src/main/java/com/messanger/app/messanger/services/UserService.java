package com.messanger.app.messanger.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.messanger.app.messanger.dto.UserSearchResponse;
import com.messanger.app.messanger.entity.AppUser;
import com.messanger.app.messanger.repository.UserRepository;


@Service
public class UserService  {

     @Autowired  
     private JwtService jwtService;
     @Autowired(required=true)
     private UserRepository userRepository;
     
     public List<UserSearchResponse> searchUsers(String token, String keyword) {
        if (!jwtService.validateToken(token)) {
            throw new RuntimeException("Invalid token");
        }

        String currentUsername = jwtService.extractUsername(token);

        return userRepository.searchByKeyword(keyword).stream()
                .filter(u -> !u.getUsername().equalsIgnoreCase(currentUsername))
                .map(UserSearchResponse::new)
                .toList();
    }


      public AppUser updateUser(String token, AppUser updatedUserData) {
        String userId = jwtService.extractUsername(token);
        return userRepository.findByUsername(userId)
                .map(existingUser -> {
                    existingUser.setUsername(updatedUserData.getUsername());
                    existingUser.setEmail(updatedUserData.getEmail());
                    existingUser.setFirstName(updatedUserData.getFirstName());
                    existingUser.setLastName(updatedUserData.getLastName());
                    existingUser.setProfile(updatedUserData.getProfile());
                    existingUser.setBio(updatedUserData.getBio());
                    return userRepository.save(existingUser);  
                })
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }
}
