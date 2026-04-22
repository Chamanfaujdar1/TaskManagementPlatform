package com.flowboard.auth.serviceimpl;

import com.flowboard.auth.config.JwtUtil;
import com.flowboard.auth.entity.User;
import com.flowboard.auth.repository.UserRepository;
import com.flowboard.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        user.setRole("MEMBER");
        user.setIsActive(true);
        user.setProvider("LOCAL");
        return userRepository.save(user);
    }

    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtUtil.generateToken(user.getEmail(), user.getRole());
    }

    @Override
    public void logout(String token) {
        // JWT is stateless — Redis blacklist can be added later
    }

    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    @Override
    public String refreshToken(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }
        String email = jwtUtil.extractEmail(token);
        String role = jwtUtil.extractRole(token);
        return jwtUtil.generateToken(email, role);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public User updateProfile(int id, User updatedUser) {
        User existing = getUserById(id);
        if (updatedUser.getFullName() != null) {
            existing.setFullName(updatedUser.getFullName());
        }
        if (updatedUser.getUsername() != null) {
            existing.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getAvatarUrl() != null) {
            existing.setAvatarUrl(updatedUser.getAvatarUrl());
        }
        return userRepository.save(existing);
    }

    @Override
    public void changePassword(int id, String newPassword) {
        User user = getUserById(id);
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void deactivateAccount(int id) {
        User user = getUserById(id);
        user.setIsActive(false);
        userRepository.save(user);
    }

    @Override
    public List<User> searchUsers(String query) {
        return userRepository.searchByFullName(query);
    }
}