package com.kdg.hexa_delivery.domain.user.controller;

import com.kdg.hexa_delivery.domain.user.dto.LoginRequestDto;
import com.kdg.hexa_delivery.domain.user.dto.SignupRequestDto;
import com.kdg.hexa_delivery.domain.user.dto.SignupResponseDto;
import com.kdg.hexa_delivery.domain.user.dto.UserDeleteRequestDto;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.domain.user.service.UserService;
import com.kdg.hexa_delivery.global.constant.Const;
import com.kdg.hexa_delivery.global.exception.BadValueException;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    UserController (UserService userService){
        this.userService = userService;
    }

    /**
     * 회원가입 API
     *
     * @param signupRequestDto 생성할 회원 정보 dto
     *
     * @return ResponseEntity<SignupResponseDto> 저장된 회원 정보 전달
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {

        if (!signupRequestDto.getPassword().equals(signupRequestDto.getPasswordConfirm())){
            throw new BadValueException(ExceptionType.PASSWORD_NOT_CORRECT);
        }

        SignupResponseDto signupResponseDto = userService.saveUser(
                signupRequestDto.getRole(),
                signupRequestDto.getEmail(),
                signupRequestDto.getPassword(),
                signupRequestDto.getName(),
                signupRequestDto.getPhone()
                );

        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponseDto);
    }

    /**
     * 로그인 API
     *
     * @param loginRequestDto 로그인 정보(아이디, 비밀번호) dto
     * @param servletRequest 세션 생성을 위한 servletRequest
     *
     * @return 로그인 성공 문구 전달
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest servletRequest) {
        servletRequest.getSession().invalidate();

        //(테스트용) 잔류 세션 제거
        servletRequest.getSession().invalidate();

        //현재의 세션 유무 확인
        if (servletRequest.getSession(false) != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        //아이디, 비밀번호 확인 후 유저 반환
        User loginedUser = userService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        //세션 생성
        HttpSession session = servletRequest.getSession();

        //세션 키 & Value 설정
        session.setAttribute(Const.LOGIN_USER, loginedUser);

        return ResponseEntity.status(HttpStatus.OK).body("로그인되었습니다.");
    }

    /**
     * 로그아웃 API
     *
     * @param servletRequest 세션 정보
     *
     * @return 로그아웃 성공 문구 전달
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest servletRequest) {

        //현재 세션 조회
        HttpSession session = servletRequest.getSession(false);



        //세션 삭제
        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.status(HttpStatus.OK).body("로그아웃되었습니다.");
    }

    /**
     * 회원탈퇴 API
     *
     * @param userDeleteRequestDto 회원 비밀번호
     * @param servletRequest 세션
     *
     * @return 회원탈퇴 성공 문구 전달
     */
    @PatchMapping
    public ResponseEntity<String> deleteUser(
            @RequestBody UserDeleteRequestDto userDeleteRequestDto,
            HttpServletRequest servletRequest
    ){
        //세션 가져오기
        HttpSession session = servletRequest.getSession(false);

        //세션 유저 정보
        User user = (User) session.getAttribute(Const.LOGIN_USER);

        //회원정보 삭제
        userService.deleteUser(user, userDeleteRequestDto.getPassword());

        //세션 삭제
        servletRequest.getSession(false).invalidate();

        return ResponseEntity.status(HttpStatus.OK).body("회원탈퇴가 완료되었습니다.");
    }

}
