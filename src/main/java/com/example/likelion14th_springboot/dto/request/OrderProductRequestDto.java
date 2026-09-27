package com.example.likelion14th_springboot.dto.request;

import lombok.Getter;

@Getter
public class OrderProductRequestDto {
    private Long productId;
    private Integer quantity;
}
