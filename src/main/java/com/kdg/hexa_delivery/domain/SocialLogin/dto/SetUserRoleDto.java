package com.kdg.hexa_delivery.domain.SocialLogin.dto;

import com.kdg.hexa_delivery.domain.user.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SetUserRoleDto {

    @NotNull
    private final Role role;

    public SetUserRoleDto(Role role) {
        this.role = role;
    }
}
