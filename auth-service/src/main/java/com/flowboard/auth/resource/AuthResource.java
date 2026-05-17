package com.flowboard.auth.resource;

import com.flowboard.auth.dto.UserDto;
import com.flowboard.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthResource {

    private final AuthService authService;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        UserDto registered = authService.register(userDto);
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
    public ResponseEntity<UserDto> getProfile(@PathVariable int id) {
        UserDto user = authService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // UPDATE PROFILE
    @PutMapping("/profile/{id}")
    public ResponseEntity<UserDto> updateProfile(@PathVariable int id,
                                              @RequestBody UserDto userDto) {
        UserDto updated = authService.updateProfile(id, userDto);
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
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String query) {
        List<UserDto> users = authService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    // DEACTIVATE ACCOUNT
    @PutMapping("/deactivate/{id}")
    public ResponseEntity<String> deactivateAccount(@PathVariable int id) {
        authService.deactivateAccount(id);
        return ResponseEntity.ok("Account deactivated successfully");
    }

    // REACTIVATE ACCOUNT
    @PutMapping("/reactivate/{id}")
    public ResponseEntity<String> reactivateAccount(@PathVariable int id) {
        authService.reactivateAccount(id);
        return ResponseEntity.ok("Account reactivated successfully");
    }

    // GET USER BY EMAIL
    @GetMapping("/email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        UserDto user = authService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    // GET TOTAL COUNT
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalUsersCount() {
        return ResponseEntity.ok(authService.getTotalUsersCount());
    }

    // GET ALL USERS
    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(authService.getAllUsers());
    }
}