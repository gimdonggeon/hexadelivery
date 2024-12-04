package com.kdg.hexa_delivery.domain.user.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequestDto {

    @NotNull
    private final String loginId;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,15}$")
    private final String password;

    public LoginRequestDto(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }
}
