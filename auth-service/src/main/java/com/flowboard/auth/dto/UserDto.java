package com.flowboard.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Integer userId;
    private String fullName;
    private String email;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @JsonAlias({"password", "passwordHash"})
    private String password;
    
    private String username;
    private String role;
    private String avatarUrl;
    private String provider;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
