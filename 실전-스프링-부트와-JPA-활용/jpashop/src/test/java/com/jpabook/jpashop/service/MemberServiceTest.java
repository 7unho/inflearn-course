package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // JUnit 실행 시, Spring과 엮어서 실행할래
@SpringBootTest // Spring 컨테이너 내에서 테스트 실행
@Transactional
public class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("회원가입")
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("김");

        //when
        Long saveId = memberService.join(member);

        //then
        em.flush();
        assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test(expected = IllegalStateException.class)
    @DisplayName("중복 회원 예외")
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("김1");

        Member member2 = new Member();
        member2.setName("김1");

        //when
        memberService.join(member1);
        memberService.join(member2); // 예외 발생 지점
//        try {
//            memberService.join(member2); // 예외 발생 지점
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        fail("예외가 발생해야 함 !!!");
    }
}