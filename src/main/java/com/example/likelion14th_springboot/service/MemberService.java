package com.example.likelion14th_springboot.service;

import com.example.likelion14th_springboot.domain.Member;
import com.example.likelion14th_springboot.dto.request.JoinRequestDto;
import com.example.likelion14th_springboot.exception.DuplicateMemberException;
import com.example.likelion14th_springboot.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private static final int ADULT_AGE = 20; // 성인 기준 나이

    private final MemberRepository memberRepository;


    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() ->
                        new IllegalArgumentException("해당 이메일의 회원이 존재하지 않습니다.")
                );
    }

    public Page<Member> getMembersByPage(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "id")
        );

        return (Page<Member>) memberRepository.findAll(pageRequest);
    }

    // 나이가 20살 이상인 회원만 이름 오름차순으로 정렬해서 페이징 조회
    public Page<Member> getAdultMembersSortedByName(Pageable pageable) {
        return memberRepository.findByAgeGreaterThanEqualOrderByNameAsc(ADULT_AGE, pageable);
    }

    // 이름이 prefix로 시작하는 회원만 조회
    public List<Member> getMembersByNamePrefix(String prefix) {
        return memberRepository.findByNameStartingWithOrderByNameAsc(prefix);
    }

    // 비밀번호 인코더 DI(생성자 주입)
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public void join(JoinRequestDto joinRequestDto) {
        // 해당 name이 이미 존재하는 경우
        if (memberRepository.existsByName(joinRequestDto.getName())) {
            throw new DuplicateMemberException("이미 존재하는 사용자입니다.");
        }

        // 유저 객체 생성
        Member member = joinRequestDto.toEntity(bCryptPasswordEncoder);

        // 유저 정보 저장
        memberRepository.save(member);
    }
}
