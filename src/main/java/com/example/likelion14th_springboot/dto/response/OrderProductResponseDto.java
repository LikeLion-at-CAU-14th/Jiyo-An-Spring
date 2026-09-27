package com.example.likelion14th_springboot.dto.response;

import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OrderProductResponseDto {
    private Long productId;
    private String productName;
    private Integer price;
    private Integer quantity;

    public static OrderProductResponseDto fromEntity(ProductOrders productOrders) {
        return OrderProductResponseDto.builder()
                .productId(productOrders.getProduct().getId())
                .productName(productOrders.getProduct().getName())
                .price(productOrders.getProduct().getPrice())
                .quantity(productOrders.getQuantity())
                .build();
    }
}
