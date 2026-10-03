package com.example.likelion14th_springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 이미 존재하는 회원으로 가입 요청 시 409 Conflict 반환
    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateMember(DuplicateMemberException e) {
        Map<String, String> response = new HashMap<>();
        response.put("message", e.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }
}
