package com.kdg.hexa_delivery.domain.user.dto;

import com.kdg.hexa_delivery.domain.base.enums.Role;
import com.kdg.hexa_delivery.domain.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SignupResponseDto {

    private Long userId;

    private Role role;

    private String name;

    private LocalDateTime createdAt;

    public SignupResponseDto(Long userId, Role role, String name, LocalDateTime createdAt) {
        this.userId = userId;
        this.role = role;
        this.name = name;
        this.createdAt = createdAt;
    }

    public static SignupResponseDto toDto(User savedUser) {
        return new SignupResponseDto(savedUser.getId(), savedUser.getRole(), savedUser.getName(), savedUser.getCreatedAt());
    }
}
