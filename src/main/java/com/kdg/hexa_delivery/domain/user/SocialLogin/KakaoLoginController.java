package com.kdg.hexa_delivery.domain.user.SocialLogin;

import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.domain.user.service.UserService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users/login")
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;
    private final KakaoApi kakaoApi;
    private final UserService userService;

    @Autowired
    public KakaoLoginController(KakaoLoginService kakaoLoginService, KakaoApi kakaoApi, UserService userService) {
        this.kakaoLoginService = kakaoLoginService;
        this.kakaoApi = kakaoApi;
        this.userService = userService;
    }

    /**
     * 카카오톡 소셜 로그인 요청 API -> 프론트에서 인가코드를 요청
     * https://kauth.kakao.com/oauth/authorize?client_id={$"kakao.api.key"}&redirect_uri=${kakao.redirect_uri}&response_type=code
     * -> /kakaoRedirect로 String code 형식으로 인가 코드를 담아서 보내준다.
     *
     * @param model
     * @return
     */
    @GetMapping("/kakao")
    public String kakaoLogin1(Model model) {

        model.addAttribute("kakaoApiKey", kakaoApi.getKakaoApiKey());
        model.addAttribute("kakaoRedirectUri",kakaoApi.getKakaoRedirectUri());

        return "로그인을 요청합니다.";
    }

    /**
     * 카카오톡 소셜 로그인 API
     *
     * @param code 카카오톡 로그인페이지에서 사용자가 로그인을 마치면 프론트가 code에 인가 코드를 담아서 보내준다.
     * @return
     */
    @GetMapping("/kakaoRedirect")
    public ResponseEntity<String> kakaoLogin(@RequestParam(required = false) String code) throws IOException, ParseException {

        try {
            String accessToken = kakaoApi.getAccessToken(code);


            HashMap<String, Object> userInfo = kakaoLoginService.getUserInfo(accessToken);

            User user = new User((String) userInfo.get("email"), (String) userInfo.get("nickname"));

            if (!userService.doesExistsByEmail(user.getEmail())) {
                userService.saveKakaoUser(user);
            }

            return ResponseEntity.status(HttpStatus.OK).body("로그인되었습니다.");
        } catch (IOException ioException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("a");
        }
    }

    @PatchMapping("/kakao/{userId}")
    public ResponseEntity<KakaoUserResponseDto> setRole(@PathVariable Long userId, @RequestBody SetUserRoleDto setUserRoleDto) {
        User userById = userService.findUserById(userId);
        userById.updateRole(setUserRoleDto.getRole());
        KakaoUserResponseDto kakaoUserResponseDto = new KakaoUserResponseDto(userId,userById.getEmail(),userById.getNickname(),userById.getRole());
        return ResponseEntity.status(HttpStatus.OK).body(kakaoUserResponseDto);
    }
}
