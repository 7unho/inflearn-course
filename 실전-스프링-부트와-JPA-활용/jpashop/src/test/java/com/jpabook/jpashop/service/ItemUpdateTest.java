package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {
    @Autowired
    EntityManager em;

    @Test
    @DisplayName("회원_수정_검증")
    public void updateTest() throws Exception {
        //given
        Book book = em.find(Book.class, 1l);

        // TX
        book.setName("TEST NAME");

        // 변경감지 == dirty checking
        // TX commit


        //then

    }
}
