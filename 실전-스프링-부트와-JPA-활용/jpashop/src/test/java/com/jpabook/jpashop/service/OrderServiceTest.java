package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.domain.item.Movie;
import com.jpabook.jpashop.exception.NotEnoughStockException;
import com.jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    @DisplayName("상품주문")
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();

        Movie movie = createMovie("기생충", "봉준호", 10);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), movie.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        // 메시지, 기댓값, 실제값
        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 (가격 * 수량) 이다.", movie.getPrice() * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", movie.getStockQuantity(), movie.getStockQuantity());
    }

    private Movie createMovie(String name, String director, int stockQuantity) {
        Movie movie = new Movie();
        movie.setName(name);
        movie.setDirector(director);
        movie.setStockQuantity(stockQuantity);
        movie.setPrice(15000);
        em.persist(movie);
        return movie;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("김준호");
        member.setAddress(new Address("서울", "강가", "1111"));
        em.persist(member);
        return member;
    }

    @Test
    @DisplayName("주문취소")
    public void 주문취소() throws Exception {
        //given
        Member member = createMember();
        Movie movie = createMovie("기생충", "김준호", 100);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), movie.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소시 상태는 CANCEL 이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 재고가 복구돼야 한다.", 100, movie.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class)
    @DisplayName("상품주문_재고초과")
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item = createMovie("기생충", "봉준호", 10);

        int orderCount = 11;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고 수량 부족 예외가 발생해야 한다");
    }
}