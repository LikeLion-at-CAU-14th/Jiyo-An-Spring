package com.example.likelion14th_springboot.dto.request;

import com.example.likelion14th_springboot.domain.ShippingAddress;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCreateRequestDto {
    private Long buyerId;
    private List<OrderProductRequestDto> products;
    private String recipient;
    private String phoneNumber;
    private String roadAddress;
    private String detailAddress;
    private String zipCode;

    public ShippingAddress toShippingAddress() {
        return ShippingAddress.builder()
                .recipient(this.recipient)
                .phoneNumber(this.phoneNumber)
                .roadAddress(this.roadAddress)
                .detailAddress(this.detailAddress)
                .zipCode(this.zipCode)
                .build();
    }
}
