package com.kdg.hexa_delivery.domain.user.SocialLogin;

import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.domain.user.service.UserService;
import com.kdg.hexa_delivery.global.constant.Const;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
     * 카카오톡 소셜 로그인 요청 API : Model에 필요한 민감정보를 담아 프론트에 전달 -> 프론트에서 인가코드를 요청
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
    public ResponseEntity<String> kakaoLogin(@RequestParam(required = false) String code, HttpServletRequest servletRequest) throws IOException, ParseException {

        //(테스트용) 잔류 세션 제거
        servletRequest.getSession().invalidate();

        try {
            //접근토큰 발급
            String accessToken = kakaoApi.getAccessToken(code);

            //발급받은 토큰으로 사용자 정보 조회
            HashMap<String, Object> userInfo = kakaoLoginService.getUserInfo(accessToken);

            //조회한 사용자 정보를 바탕으로 사용자 객체 생성
            User user = new User((String) userInfo.get("email"), (String) userInfo.get("nickname"));

            //db에 사용자가 존재하지 않는다면 저장
            if (!userService.doesExistsByEmail(user.getEmail())) {
                userService.saveKakaoUser(user);
            }

            //현재의 세션 유무 확인
            if (servletRequest.getSession(false) != null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }

            return ResponseEntity.status(HttpStatus.OK).body("로그인되었습니다.");
        } catch (IOException ioException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당하는 정보를 찾을 수 없습니다.");
        }
    }

    @PostMapping("/kakao/{userId}")
    public ResponseEntity<KakaoUserResponseDto> setRole(@PathVariable Long userId, @Valid @RequestBody SetUserRoleDto setUserRoleDto) {
        User userById = userService.findUserById(userId);
        userById.updateRole(setUserRoleDto.getRole());
        KakaoUserResponseDto kakaoUserResponseDto = new KakaoUserResponseDto(userId,userById.getEmail(),userById.getNickname(),userById.getRole());
        return ResponseEntity.status(HttpStatus.OK).body(kakaoUserResponseDto);
    }

    @PostMapping("/kakao/logout")
    public ResponseEntity<String> kakaoLogout(@RequestHeader String Authorization){
        kakaoLoginService.kakaoLogout(Authorization.substring(7));

        return ResponseEntity.status(HttpStatus.OK).body("로그아웃되었습니다.");
    }
}
