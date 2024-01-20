package com.jpabook.jpashop.repository.order.simplequery;

import com.jpabook.jpashop.repository.order.OrderSimpleQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {
    private final EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "SELECT new com.jpabook.jpashop.repository.order.OrderSimpleQueryDto(o.id, o.orderDate, o.status, m.name, d.address) " +
                        "FROM Order o " +
                        "JOIN o.member m JOIN o.delievery d", OrderSimpleQueryDto.class).getResultList();
    }
}
