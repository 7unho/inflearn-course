package com.jpabook.jpashop.service;

import com.jpabook.jpashop.MemberRepository;
import com.jpabook.jpashop.domain.Member;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // JPA 데이터 변경 시 무족건 ! 클래스 내 public 메서드에 기본적으로 먹힘
@RequiredArgsConstructor // final 필드에만 autowired
public class MemberService {
    private final MemberRepository memberRepository;

    // constructor injection
    // 최신 버전 스프링에서는 생성자가 하나일 경우, 자동으로 autowired 해줌. -> 생략 가능
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional
    public Long join(Member member){
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        // 중복 회원 Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) throw new IllegalStateException("이미 존재하는 회원입니다.");
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
