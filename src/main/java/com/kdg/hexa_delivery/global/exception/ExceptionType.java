package com.kdg.hexa_delivery.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ExceptionType {

    EXIST_USER(HttpStatus.BAD_REQUEST, "동일한 email 의 사용자가 존재합니다."),
    PASSWORD_NOT_CORRECT(HttpStatus.BAD_REQUEST,  "비밀번호가 일치하지 않습니다."),
    PASSWORD_SAME(HttpStatus.BAD_REQUEST, "기존의 비밀번호와 일치합니다."),
    DELETED_MENU(HttpStatus.BAD_REQUEST, "이미 삭제된 메뉴입니다."),
    DELETED_USER(HttpStatus.BAD_REQUEST, "이미 삭제된 유저입니다."),
    DELETED_STORE(HttpStatus.BAD_REQUEST, "이미 폐업된 가게입니다."),
    USER_NOT_MATCH(HttpStatus.BAD_REQUEST, "잘못된 유저의 정보에 접근하고 있습니다."),
    NOT_YET_DELIVERED(HttpStatus.BAD_REQUEST, "아직 배달이 완료되지 않은 주문건입니다."),
    NOT_REJECT_ORDER(HttpStatus.BAD_REQUEST, "수락한 주문은 거절하실 수 없습니다."),
    NOT_AMOUNT_MIN(HttpStatus.BAD_REQUEST, "최소 주문 금액보다 높아야 합니다."),
    NOT_OPEN_STORE(HttpStatus.BAD_REQUEST, "가게 영업시간이 아닙니다."),
    CHANGE_STATUS_SEQUENTIAL(HttpStatus.BAD_REQUEST, "상태 변경은 각 단계를 순차적으로만 진행할 수 있습니다."),
    BAD_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호는 영문자, 숫자, 특수문자를 포함하며 8자 이상이어야 합니다."),
    NOT_LOGIN(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    CUT_OF_SESSION(HttpStatus.UNAUTHORIZED, "세션이 끊어졌습니다."),
    NEED_AUTHORITY_ADMIN(HttpStatus.UNAUTHORIZED, "ADMIN 권한이 필요합니다."),
    NEED_AUTHORITY_OWNER(HttpStatus.UNAUTHORIZED, "OWNER 권한이 필요합니다."),
    NEED_AUTHORITY_CUSTOMER(HttpStatus.UNAUTHORIZED, "CUSTOMER 권한이 필요합니다."),
    ALREADY_LOGIN(HttpStatus.UNAUTHORIZED, "이미 로그인한 사용자입니다."),
    ALREADY_REVIEW(HttpStatus.BAD_REQUEST, "이미 작성한 주문 건입니다."),
    ALREADY_REQUEST_ADVERTISE(HttpStatus.BAD_REQUEST, "이미 처리한 광고 요청입니다."),
    ACCESS_OTHER_CART(HttpStatus.UNAUTHORIZED, "다른 유저의 장바구니에 접근하고 있습니다."),
    ACCESS_OTHER_STORE(HttpStatus.UNAUTHORIZED, "다른 가게에 접근하고 있습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,  "해당 유저의 정보를 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 가게 정보를 찾을 수 없습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문 정보를 찾을 수 없습니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴 정보를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이미지 정보를 찾을 수 없습니다."),
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 쿠폰 정보를 찾을 수 없습니다."),
    ADVERTISE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 광고 정보를 찾을 수 없습니다."),
    REVIEW_NOT_FOUND(HttpStatus.BAD_REQUEST, "해당 리뷰을 찾을 수 없습니다."),
    STORE_MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "가게의 메뉴가 없습니다."),
    STORE_OVER_THREE(HttpStatus.BAD_REQUEST, "영업중인 가게가 3개 이상입니다. 더 이상 생성 할 수 없습니다."),
    EXPIRATION(HttpStatus.NOT_FOUND, "기간이 만료되었습니다."),
    NOT_AMOUNT(HttpStatus.NOT_FOUND, "수량이 다 떨어졌습니다."),
    FAIL_IMAGE(HttpStatus.BAD_REQUEST,  "해당 파일 업로드에 실패하였습니다.");

    private final HttpStatus status;
    private final String message;

    ExceptionType(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
