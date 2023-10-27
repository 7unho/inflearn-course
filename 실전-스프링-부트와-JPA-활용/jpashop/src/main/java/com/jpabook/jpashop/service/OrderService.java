package com.jpabook.jpashop.service;

import com.jpabook.jpashop.domain.Delievery;
import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.domain.item.Item;
import com.jpabook.jpashop.repository.ItemRepository;
import com.jpabook.jpashop.repository.MemberRepository;
import com.jpabook.jpashop.repository.OrderRepository;
import com.jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //== 주문 ==//
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 1. 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 2. 배송정보 생성
        Delievery delievery = new Delievery();
        delievery.setAddress(member.getAddress());

        // 3. 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 4. 주문 생성
        Order order = Order.createOrder(member, delievery, orderItem);

        // 5. 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        // 배송 정보 조회
        Order order = orderRepository.findOne(orderId);

        // 주문 취소
        // 스프링에서 더티체킹으로 인해, 데이터의 변경이 일어나면 DB에 update 쿼리를 자동으로 날린다.
        // 따라서, 부가적인 SQL 코드를 짤 필요가 없어진다.
        order.cancel();
    }

    /**
     * 검색
     */
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
