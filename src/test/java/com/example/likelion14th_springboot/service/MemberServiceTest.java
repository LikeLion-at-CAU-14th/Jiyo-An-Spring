package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.enums.Role;
import com.example.likelion14th_springboot.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

import java.util.List;
import java.util.stream.IntStream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL) // 생성자 주입용
public class MemberServiceTest {

    private final MemberService memberService;

    private final MemberRepository memberRepository;

    MemberServiceTest(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @BeforeEach
    void setUp() {
        // DB 모든 Member 데이터 삭제
        // - 테스트 환경 (H2 등 인메모리 DB) : 사용 O
        // - 실제 운영 (Production) DB : 사용 X
        memberRepository.deleteAll();

        IntStream.rangeClosed(1, 30).forEach(i -> {
            Member member = Member.builder()
                    .name("user" + i)
                    .email("user" + i + "@test.com")
                    .address("서울시 테스트동 " + i + "번지")
                    .phoneNumber("010-1234-56" + String.format("%02d", i))
                    .age(10 + i) // user1~user9는 미성년(11~19), user10~user30은 성인(20~40)
                    .deposit(1000 * i)
                    .isAdmin(false)
                    .role(Role.BUYER)
                    .build();

            memberRepository.save(member);
        });
    }

    @Test
    @DisplayName("모든 회원을 조회한다.")
    void testGetAllMembers() {
        List<Member> memberList = memberService.getAllMembers();
        assertEquals(30, memberList.size());

        Member member = memberList.stream()
                .filter(m -> m.getName().equals("user15"))
                .findFirst()
                .orElseThrow();
    }

    @Test
    @DisplayName("이메일로 회원을 조회한다.")
    void testGetByEmail() {
        Member actual = memberService.getByEmail("user1@test.com");

        assertEquals(actual.getName(), "user1");
    }

    @Test
    @DisplayName("이메일로 없는 회원을 조회할 경우 예외를 던진다.")
    void ThrowsExceptionIfEmailDoesNotExist() {
        assertThrows(IllegalArgumentException.class, () -> {
            memberService.getByEmail("none@test.com");
        });
    }

    @Test
    @DisplayName("회원 목록을 ID 기준으로 내림차순 조회 시 페이지 정보가 올바르게 반환된다.")
    void testGetMembersByPage() {
        Page<Member> page = memberService.getMembersByPage(0, 10);

        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getTotalElements()).isEqualTo(30);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getContent().get(0).getName()).isEqualTo("user30");
    }

    @Test
    @DisplayName("이름이 주어진 접두사로 시작하는 회원만 조회한다.")
    void testGetMembersByNamePrefix() {
        String prefix = "user1";

        List<Member> memberList = memberService.getMembersByNamePrefix(prefix);
        // user1, user10 ~ user19 조회 확인
        assertThat(memberList).hasSize(11);
        assertThat(memberList).extracting(Member::getName).startsWith("user1", "user10", "user11");
        assertThat(memberList).allMatch(member -> member.getName().startsWith(prefix));
    }

    @Test
    @DisplayName("나이가 20살 이상인 회원만 이름 오름차순으로 정렬해서 페이징 조회한다.")
    void testGetAdultMembersSortedByName() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Member> page = memberService.getAdultMembersSortedByName(pageable);

        // 20살 이상은 user10 ~ user30 총 21명이고 이름 오름차순 첫 페이지는 user10 ~ user19
        assertThat(page.getTotalElements()).isEqualTo(21);
        assertThat(page.getTotalPages()).isEqualTo(3);
        assertThat(page.getContent()).hasSize(10);
        assertThat(page.getContent()).extracting(Member::getName)
                .containsExactly("user10", "user11", "user12", "user13", "user14",
                        "user15", "user16", "user17", "user18", "user19");
        assertThat(page.getContent()).allMatch(member -> member.getAge() >= 20);
    }
}
