package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderItem;
import com.jpabook.jpashop.repository.order.OrderDto;
import com.jpabook.jpashop.repository.order.OrderRepository;
import com.jpabook.jpashop.repository.order.OrderSearch;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    /**
     * 간단한 주문 조회 V1: 엔티티를 직접 노출
     * [ Hibernate5 모듈 적용 안하면 StackOverflow 발생됨 ]
     * @return 주문 내역
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // Lazy Loading Init
        for(Order order: orders) {
            order.getMember().getName();
            order.getDelievery().getAddress();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }

        return orders;
    }

    /**
     * 간단한 주문 조회 V2: 엔티티를 DTO로 변환
     * @return 주문 내역
     */
    @GetMapping("/api/v2/orders")
    public Result ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> result =  orders.stream()
                                       .map(OrderDto::new)
                                       .collect(Collectors.toList());

        return new Result(result);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
