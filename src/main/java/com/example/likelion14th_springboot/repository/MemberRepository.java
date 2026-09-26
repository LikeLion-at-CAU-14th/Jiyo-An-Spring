package com.example.likelion14th_springboot.repository;

import com.example.likelion14th_springboot.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    // age >= 기준 나이 인 회원만 조회하고, 이름 오름차순으로 정렬해서 페이징
    Page<Member> findByAgeGreaterThanEqualOrderByNameAsc(Integer age, Pageable pageable);

    // 이름이 prefix로 시작하는 회원만 조회 (LIKE 'prefix%')
    List<Member> findByNameStartingWithOrderByNameAsc(String prefix);
}
