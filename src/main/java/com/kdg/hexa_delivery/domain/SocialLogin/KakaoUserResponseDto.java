package com.kdg.hexa_delivery.domain.SocialLogin;

import com.kdg.hexa_delivery.domain.user.enums.Role;
import lombok.Getter;

@Getter
public class KakaoUserResponseDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final Role role;

    public KakaoUserResponseDto(Long id, String email, String nickname, Role role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.role = role;
    }
}
