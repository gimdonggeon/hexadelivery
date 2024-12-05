package com.kdg.hexa_delivery.domain.user.dto;

import com.kdg.hexa_delivery.domain.base.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotNull
    private final Role role;

    @NotNull
    private final String loginId;

    @NotNull
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")
    private final String email;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*?_]).{8,15}$")
    private final String password;

    @NotNull
    private final String passwordConfirm;

    @NotNull
    private final String name;

    @NotNull
    private final String phone;

    public SignupRequestDto(Role role, String loginId, String email, String password, String passwordConfirm, String name, String phone) {
        this.role = role;
        this.loginId = loginId;
        this.email = email;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.name = name;
        this.phone = phone;
    }
}
