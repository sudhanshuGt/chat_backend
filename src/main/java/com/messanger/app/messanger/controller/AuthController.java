package com.messanger.app.messanger.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.messanger.app.messanger.dto.LoginRequest;
import com.messanger.app.messanger.dto.SignupRequest;
import com.messanger.app.messanger.dto.UserSearchResponse;
import com.messanger.app.messanger.entity.AppUser;
import com.messanger.app.messanger.repository.UserRepository;
import com.messanger.app.messanger.services.JwtService;
import com.messanger.app.messanger.services.UserService;
 
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired(required=true)
    private UserService userService;

  
@Autowired
private JwtService jwtService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody SignupRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }

        AppUser user = new AppUser();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setProfile("https://cdn-icons-png.flaticon.com/512/6897/6897018.png");
        user.setBio(request.getBio());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
    }
  

    
    @PostMapping("/profile")
    public ResponseEntity<AppUser> updateUser( @RequestHeader("Authorization") String authHeader, @RequestBody AppUser updatedUser) {
       
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        AppUser user = userService.updateUser(token, updatedUser);
        return ResponseEntity.ok(user);
    }


@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    Optional<AppUser> userOpt = userRepository.findByUsername(request.getUsername());

    if (userOpt.isPresent()) {
        AppUser user = userOpt.get();
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user.getUsername());

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("profile", user.getProfile());
            response.put("email", user.getEmail());
            response.put("firstName", user.getFirstName());
            response.put("lastName", user.getLastName());
            response.put("bio", user.getBio());

            return ResponseEntity.ok(response);
        }
    }
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
}



    @GetMapping("/search")
    public ResponseEntity<List<UserSearchResponse>> searchUsers(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("username") String keyword) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        try {
            List<UserSearchResponse> result = userService.searchUsers(token, keyword);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
