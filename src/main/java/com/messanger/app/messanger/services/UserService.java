package com.messanger.app.messanger.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.messanger.app.messanger.dto.UserSearchResponse;
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
}
