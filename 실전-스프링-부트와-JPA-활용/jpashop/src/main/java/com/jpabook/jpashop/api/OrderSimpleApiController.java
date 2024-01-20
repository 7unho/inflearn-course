package com.jpabook.jpashop.api;

import com.jpabook.jpashop.domain.Address;
import com.jpabook.jpashop.domain.Order;
import com.jpabook.jpashop.domain.OrderStatus;
import com.jpabook.jpashop.repository.order.OrderRepository;
import com.jpabook.jpashop.repository.order.OrderSearch;
import com.jpabook.jpashop.repository.order.OrderSimpleQueryDto;
import com.jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * XtoOne 관계의 성능 최적화
 * Order
 * Order -> Member
 * Order -> Delievery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    /**
     * 쿼리 방식 선택 순서
     * 1. 엔티티 -> DTO로 변환하는 방법을 우선 선택 := V2
     * 2. 필요하다면 Fetch Join으로 성능 최적화 := V3
     * 3. 그래도 안되면 DTO로 직접 조회하는 방법 사용 := V4
     * 4. 마지막 방법으로 JPA가 제공하는 Native SQL이나 Spring JDBC Template을 사용해 SQL 직접 사용
     */
    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /**
     * 간단한 주문 조회 V1: 엔티티를 직접 노출
     * @return 주문 내역
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    /**
     * 간단한 주문 조회 V2: 엔티티를 DTO로 변환
     * @return 주문 내역
     */
    @GetMapping("/api/v2/simple-orders")
    public Result orderV2() {
        // [N + 1] 문제
        // 1. 2개의 row가 조회됨
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // 2. 1번에서 조회된 row수 만큼 loop 진행
        //    -> row수 만큼 LAZY 초기화가 진행되고, 그만큼 DB로의 쿼리수가 많아진다.
        //    따라서, 총 5번의 쿼리가 발생( order 조회, 각 order별 member, delievery LAZY 초기화에 따른 2번의 쿼리 조회 )
        List<SimpleOrderDto> result = orders.stream()
                                            .map(SimpleOrderDto::new)
                                            .collect(Collectors.toList());

        return new Result(result);
    }

    /**
     * 간단한 주문 조회 V3: Fetch Join 최적화
     * @return
     */
    @GetMapping("/api/v3/simple-orders")
    public Result orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelievery();
        List<SimpleOrderDto> result = orders.stream()
                                            .map(SimpleOrderDto::new)
                                            .collect(Collectors.toList());

        return new Result(result);
    }

    /**
     * 간단한 주문 조회 V4: DTO로 바로 조회
     * @return
     */
    @GetMapping("/api/v4/simple-orders")
    public Result orderV4() {
        List<OrderSimpleQueryDto> result = orderSimpleQueryRepository.findOrderDtos();

        return new Result(result);
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            this.orderId = order.getId();
            this.orderDate = order.getOrderDate();
            this.orderStatus = order.getStatus();

            /**
             * LAZY 초기화
             * 영속성 컨텍스트에서 해당 멤버의 아이디, 해당 딜리버리의 아이디로 값을 각각 값을 찾아봄
             * -> 영속성 컨텍스트 내에 일치하는 데이터가 없다면 DB로 쿼리를 날리게 됨.
             */
            this.name = order.getMember().getName();
            this.address = order.getDelievery().getAddress();
        }
    }

    /**
     * JSON 규격화 클래스
     * @param <T>
     */
    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }
}
