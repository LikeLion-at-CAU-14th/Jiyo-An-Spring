package com.example.likelion14th_springboot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping("/v1")
    public Map<String, String> healthCheck() {
        // response를 만드는 방식
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");

        return response;
    }
}