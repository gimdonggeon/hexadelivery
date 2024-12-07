package com.kdg.hexa_delivery.domain.user.repository;

import com.kdg.hexa_delivery.domain.user.entity.User;
import com.kdg.hexa_delivery.global.exception.ExceptionType;
import com.kdg.hexa_delivery.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //로그인 아이디로 회원 존재여부 확인
    boolean existsByEmail(String email);

    //로그인 아이디로 유저 찾기
    Optional<User> findByEmail(String email);

    //예외처리가 포함된 로그인 아이디로 유저 찾기
    default User findByEmailOrElseThrow(String email){
        return findByEmail(email).orElseThrow(()-> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }

    default User findByIdOrElseThrow(Long userId){
        return findById(userId).orElseThrow(()-> new NotFoundException(ExceptionType.USER_NOT_FOUND));
    }
}
