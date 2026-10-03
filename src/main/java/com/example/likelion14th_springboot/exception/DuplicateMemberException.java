package com.example.likelion14th_springboot.exception;

public class DuplicateMemberException extends RuntimeException {

    public DuplicateMemberException(String message) {
        super(message);
    }
}
