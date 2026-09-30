package com.example.likelion14th_springboot.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress {

    private String recipient; // 수령인
    private String phoneNumber;
    private String roadAddress; // 도로명주소
    private String detailAddress; // 상세주소
    private String zipCode; // 우편번호
}
