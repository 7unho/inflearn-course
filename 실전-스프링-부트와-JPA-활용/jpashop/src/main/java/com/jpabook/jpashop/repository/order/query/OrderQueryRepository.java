package com.jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    private final EntityManager em;
    
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();
        // orderId로 orderItemList 생성
        result.forEach(o -> {
           List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
           o.setOrderItems(orderItems);
        });

        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "SELECT new com.jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi) " +
                        "FROM OrderItem oi " +
                        "JOIN oi.item i " +
                        "WHERE oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "SELECT new com.jpabook.jpashop.repository.order.query.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) " +
                        "FROM Order o " +
                        "JOIN o.member m " +
                        "JOIN o.delievery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDtoVersion5() {
        List<OrderQueryDto> result = findOrders();

        // 1. OrderId들을 result에서 추출.
        List<Long> orderIds = result.stream().map(o -> o.getOrderId()).collect(Collectors.toList());

        // 2. 1에서 추출한 orderIds를 통해 orderItems 정보를 추출
        List<OrderItemQueryDto> orderItems = em.createQuery(
                // TODO: 왜 쿼리가 많이 발생할까?
                // SELECT new com.jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi) -> 쿼리가 많이 발생.
                "SELECT new com.jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, oi.item.name, oi.orderPrice, oi.count) " +
                        "FROM OrderItem oi " +
                        "JOIN oi.item i " +
                        "WHERE oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // 3. 2에서 추출한 orderItems를 Map<orderId, orderItems>의 형태로 변환 시킴.
        // ? Collectors.groupingBy -> Collections의 요소를 통해 집계, 그룹핑
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream().collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

        // 4. 3을 통해 생성한 Map으로 result의 orderId 값에 해당하는 데이터를 찾아 저장.
        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }
}
