package com.messanger.app.messanger.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.messanger.app.messanger.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByEmail(String email); 
    List<AppUser> findByUsernameContainingIgnoreCase(String username);
}
