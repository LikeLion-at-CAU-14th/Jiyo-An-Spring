package com.example.likelion14th_springboot.dto.response;

import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponseDto {
    private Long id;
    private Long buyerId;
    private DeliverStatus deliverStatus;
    private String recipient;
    private String phoneNumber;
    private String roadAddress;
    private String detailAddress;
    private String zipCode;
    private List<OrderProductResponseDto> products;

    public static OrderResponseDto fromEntity(Orders orders) {
        return OrderResponseDto.builder()
                .id(orders.getId())
                .buyerId(orders.getBuyer().getId())
                .deliverStatus(orders.getDeliverStatus())
                .recipient(orders.getShippingAddress().getRecipient())
                .phoneNumber(orders.getShippingAddress().getPhoneNumber())
                .roadAddress(orders.getShippingAddress().getRoadAddress())
                .detailAddress(orders.getShippingAddress().getDetailAddress())
                .zipCode(orders.getShippingAddress().getZipCode())
                .products(orders.getProductOrders().stream()
                        .map(OrderProductResponseDto::fromEntity)
                        .toList())
                .build();
    }
}
