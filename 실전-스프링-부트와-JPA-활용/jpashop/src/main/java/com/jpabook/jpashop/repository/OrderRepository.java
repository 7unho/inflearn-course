package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.Member;
import com.jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }


    /**
     * @param orderSearch
     * @return List[Order]
     * @SQL {
     *     SELECT *
     *     FROM order, member
     *     WHERE
     *          order.member_id = member.id
     *     [ and  status = orderSearch.status ]
     *     [ and  name = orderSearch.member_name ]
     * }
     *
     */
    // TODO: queryDSL로 동적 쿼리 생성하기
    public List<Order> findAll(OrderSearch orderSearch) {
        return null;
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        return em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }
}
