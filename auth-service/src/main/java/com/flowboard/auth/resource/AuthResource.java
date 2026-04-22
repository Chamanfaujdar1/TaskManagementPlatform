package com.flowboard.auth.resource;

import com.flowboard.auth.entity.User;
import com.flowboard.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthResource {

    @Autowired
    private AuthService authService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registered = authService.register(user);
        return ResponseEntity.ok(registered);
    }

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> request) {
        String token = authService.login(
                request.get("email"),
                request.get("password")
        );
        return ResponseEntity.ok(Map.of("token", token));
    }

    // LOGOUT
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        authService.logout(token);
        return ResponseEntity.ok("Logged out successfully");
    }

    // REFRESH TOKEN
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        String newToken = authService.refreshToken(request.get("token"));
        return ResponseEntity.ok(Map.of("token", newToken));
    }

    // GET PROFILE
    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getProfile(@PathVariable int id) {
        User user = authService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // UPDATE PROFILE
    @PutMapping("/profile/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable int id,
                                              @RequestBody User user) {
        User updated = authService.updateProfile(id, user);
        return ResponseEntity.ok(updated);
    }

    // CHANGE PASSWORD
    @PutMapping("/password/{id}")
    public ResponseEntity<String> changePassword(@PathVariable int id,
                                                 @RequestBody Map<String, String> request) {
        authService.changePassword(id, request.get("newPassword"));
        return ResponseEntity.ok("Password changed successfully");
    }

    // SEARCH USERS
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = authService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    // DEACTIVATE ACCOUNT
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateAccount(@PathVariable int id) {
        authService.deactivateAccount(id);
        return ResponseEntity.ok("Account deactivated successfully");
    }

    // GET USER BY EMAIL
    @GetMapping("/email")
    public ResponseEntity<User> getUserByEmail(@RequestParam String email) {
        User user = authService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}