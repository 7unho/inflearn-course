package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.domain.item.Movie;
import com.jpabook.jpashop.repository.ItemRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class) // JUnit 실행 시, Spring과 엮어서 실행할래
@SpringBootTest // Spring 컨테이너 내에서 테스트 실행
@Transactional
public class ItemServiceTest {
    @Autowired
    EntityManager em;

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("")
    public void 상품추가() throws Exception {
        //given
        Movie movie = new Movie();
        movie.setDirector("봉준호");

        //when
        Long saveId = itemService.saveItem(movie);

        //then
        em.flush();
        assertEquals(movie, itemRepository.findOne(saveId));
    }
}
