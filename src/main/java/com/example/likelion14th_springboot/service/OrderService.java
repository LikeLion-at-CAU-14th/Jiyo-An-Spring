package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.domain.Orders;
import com.example.likelion14th_springboot.domain.Product;
import com.example.likelion14th_springboot.domain.mapping.ProductOrders;
import com.example.likelion14th_springboot.dto.request.OrderCreateRequestDto;
import com.example.likelion14th_springboot.dto.request.OrderProductRequestDto;
import com.example.likelion14th_springboot.dto.request.OrderUpdateRequestDto;
import com.example.likelion14th_springboot.dto.response.OrderResponseDto;
import com.example.likelion14th_springboot.enums.DeliverStatus;
import com.example.likelion14th_springboot.repository.MemberRepository;
import com.example.likelion14th_springboot.repository.OrderRepository;
import com.example.likelion14th_springboot.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    // 주문 생성
    @Transactional
    public OrderResponseDto createOrder(OrderCreateRequestDto dto) {
        // 1. 구매자 조회
        Member buyer = memberRepository.findById(dto.getBuyerId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 구매자입니다."));

        // 2. 주문 상품 조회 + 재고 확인 + 총 주문 금액 계산
        List<Product> products = new ArrayList<>();
        int totalPrice = 0;
        for (OrderProductRequestDto productDto : dto.getProducts()) {
            Product product = productRepository.findById(productDto.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("해당 상품이 존재하지 않습니다."));

            if (product.getStock() < productDto.getQuantity()) {
                throw new IllegalArgumentException("상품 재고가 부족합니다.");
            }

            products.add(product);
            totalPrice += product.getPrice() * productDto.getQuantity();
        }

        // 3. 구매자 잔액 확인
        if (buyer.getDeposit() < totalPrice) {
            throw new IllegalArgumentException("잔액이 부족합니다.");
        }

        // 4. 주문 생성 (최초 배송상태는 PREPARATION)
        Orders orders = Orders.builder()
                .deliverStatus(DeliverStatus.PREPARATION)
                .buyer(buyer)
                .productOrders(new ArrayList<>())
                .shippingAddress(dto.toShippingAddress())
                .build();

        // 5. 주문 상품 연결 + 재고 차감
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            int quantity = dto.getProducts().get(i).getQuantity();

            product.reduceStock(quantity);
            orders.addProductOrders(ProductOrders.builder()
                    .product(product)
                    .orders(orders)
                    .quantity(quantity)
                    .build());
        }

        // 6. 잔액 차감
        buyer.useDeposit(totalPrice);

        Orders saved = orderRepository.save(orders);
        return OrderResponseDto.fromEntity(saved);
    }

    // 구매자별 주문 목록 조회
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByBuyer(Long buyerId) {
        return orderRepository.findByBuyerIdAndDeletedFalse(buyerId).stream()
                .map(OrderResponseDto::fromEntity)
                .toList();
    }

    // 주문 단건 조회
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId) {
        Orders orders = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));
        return OrderResponseDto.fromEntity(orders);
    }

    // 배송정보 수정
    @Transactional
    public OrderResponseDto updateOrder(Long orderId, OrderUpdateRequestDto dto) {
        Orders orders = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        if (orders.getDeliverStatus() != DeliverStatus.PREPARATION) {
            throw new IllegalArgumentException("배송준비 중인 주문만 수정할 수 있습니다.");
        }

        orders.updateShippingAddress(dto.toShippingAddress());

        return OrderResponseDto.fromEntity(orders);
    }

    // 주문 삭제 (Soft Delete)
    @Transactional
    public void deleteOrder(Long orderId) {
        Orders orders = orderRepository.findByIdAndDeletedFalse(orderId)
                .orElseThrow(() -> new IllegalArgumentException("해당 주문이 존재하지 않습니다."));

        if (orders.getDeliverStatus() != DeliverStatus.COMPLETED) {
            throw new IllegalArgumentException("배송완료된 주문만 삭제할 수 있습니다.");
        }

        orders.softDelete();
    }
}
