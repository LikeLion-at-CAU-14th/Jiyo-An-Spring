package com.example.likelion14th_springboot.domain;
// 프로그래밍적 기술이 아닌 비즈니스 정책이고, 순수한 자바 객체라서 도메인!

public class PasswordValidator {

    public static final String WRONG_PASSWORD_LENGTH_EXCEPTION_MESSAGE = "비밀번호는 최소 8자 이상이어야 합니다.";
    public static final String WRONG_PASSWORD_EMPTY_EXCEPTION_MESSAGE = "비밀번호는 비어있을 수 없습니다.";

    // 비밀번호가 규칙에 맞지 않으면 예외를 던지고, 맞으면 아무 일도 일어나지 않는(void) 검증 메서드
    public void validate(String password) {
        // 1. null 이거나 빈 문자열인지 방어
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException(WRONG_PASSWORD_EMPTY_EXCEPTION_MESSAGE);
        }

        // 2. 길이가 8자 미만인지 방어
        if (password.length() < 8) {
            throw new IllegalArgumentException(WRONG_PASSWORD_LENGTH_EXCEPTION_MESSAGE);
        }
    }
}