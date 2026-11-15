package com.example.likelion14th_springboot.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberDomainTest {

    @Test
    @DisplayName("회원의 예치금을 충전하고 사용할 수 있다.")
    void depositTest() {
        // Given (초기 예치금 1000원인 멤버 세팅)
        Member member = Member.builder()
                .name("멋사")
                .deposit(1000)
                .build();

        // When 1: 500원 충전
        member.chargeDeposit(500);
        // Then 1
        assertThat(member.getDeposit()).isEqualTo(1500);

        // When 2: 300원 사용
        member.useDeposit(300);
        // Then 2
        assertThat(member.getDeposit()).isEqualTo(1200);
    }
}
