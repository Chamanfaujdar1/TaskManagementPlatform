package com.flowboard.auth.service;

import com.flowboard.auth.dto.UserDto;
import java.util.List;

public interface AuthService {

    UserDto register(UserDto userDto);

    String login(String email, String password);

    void logout(String token);

    boolean validateToken(String token);

    String refreshToken(String token);

    UserDto getUserByEmail(String email);

    UserDto getUserById(int id);

    UserDto updateProfile(int id, UserDto userDto);

    void changePassword(int id, String newPassword);

    void deactivateAccount(int id);
    void reactivateAccount(int id);

    List<UserDto> searchUsers(String query);

    long getTotalUsersCount();

    List<UserDto> getAllUsers();
}