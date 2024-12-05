package com.kdg.hexa_delivery.domain.user.service;

import com.kdg.hexa_delivery.domain.base.enums.Role;
import com.kdg.hexa_delivery.domain.base.enums.Status;
import com.kdg.hexa_delivery.domain.user.dto.SignupResponseDto;
import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.domain.user.repository.UserRepository;
import com.kdg.hexa_delivery.global.config.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * 회원가입 메서드
     *
     * @param role 권한
     * @param loginId 로그인 아이디
     * @param email 이메일
     * @param password 로그인 비밀번호
     * @param name 이름
     * @param phone 휴대폰번호
     *
     * @return SignupResponseDto 저장된 회원 정보 전달
     */
    public SignupResponseDto saveUser(Role role, String loginId, String email, String password, String name, String phone) {

        //중복 아이디인지 확인
        if (userRepository.existsByLoginId(loginId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하는 아이디입니다.");
        }

        //비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(password);

        //유저 객체 생성
        User user = new User(role, loginId, email, encodedPassword, name, phone, Status.NORMAL);

        //유저 저장
        User savedUser = userRepository.save(user);

        return SignupResponseDto.toDto(savedUser);
    }

    /**
     * 로그인을 위해 아이디와 비밀번호를 확인 메서드
     *
     * @param loginId 로그인 아이디
     * @param password 로그인 비밀번호
     */
    public User login(String loginId, String password) {

        //회원 정보 불러오기
        User loginUser = userRepository.findByLoginIdOrElseThrow(loginId);

        //비밀번호 비교
        if (!passwordEncoder.matches(password, loginUser.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호를 확인하세요.");
        }

        return loginUser;
    }

    /**
     * 회원탈퇴 메서드
     *
     * @param user 세션 사용자
     * @param password 회원 비밀번호
     */
    @Transactional
    public void deleteUser(User user, String password) {

        //비밀번호 확인 후 삭제
        if (passwordEncoder.matches(password, user.getPassword())) {
            //회원의 논리적 삭제
            user.updateStatus2Delete();

            //회원 상태 저장 명시
            userRepository.save(user);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 맞지 않습니다.");
        }
    }

}
