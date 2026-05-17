package com.flowboard.auth.mapper;

import com.flowboard.auth.dto.UserDto;
import com.flowboard.auth.entity.User;

public class UserMapper {

    public static UserDto mapToDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto dto = new UserDto();
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setAvatarUrl(user.getAvatarUrl());
        dto.setProvider(user.getProvider());
        dto.setIsActive(user.getIsActive());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public static User mapToEntity(UserDto dto) {
        if (dto == null) {
            return null;
        }
        User user = new User();
        user.setUserId(dto.getUserId());
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername());
        user.setPasswordHash(dto.getPassword()); // Used during registration
        user.setRole(dto.getRole());
        user.setAvatarUrl(dto.getAvatarUrl());
        user.setProvider(dto.getProvider());
        user.setIsActive(dto.getIsActive());
        user.setCreatedAt(dto.getCreatedAt());
        return user;
    }
}
