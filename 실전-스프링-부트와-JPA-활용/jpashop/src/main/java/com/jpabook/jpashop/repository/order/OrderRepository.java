package com.jpabook.jpashop.repository.order;

import com.jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
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

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }


    /**
     * @param orderSearch
     * @return List[Order]
     * @SQL {
     * SELECT *
     * FROM order, member
     * WHERE
     * order.member_id = member.id
     * [ and  status = orderSearch.status ]
     * [ and  name = orderSearch.member_name ]
     * }
     */
    // TODO: queryDSL로 동적 쿼리 생성하기
    public List<Order> findAll(OrderSearch orderSearch) {
        return null;
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {
        return em.createQuery("SELECT o FROM Order o", Order.class).getResultList();
    }

    public List<Order> findAllWithMemberDelievery() {
        return em.createQuery(
                "SELECT o " +
                        "FROM Order o " +
                        "JOIN FETCH o.member " +
                        "JOIN FETCH o.delievery", Order.class).getResultList();
    }

    public List<Order> findAllWithMemberDelievery(int offset, int limit) {
        return em.createQuery(
                        "SELECT o " +
                                "FROM Order o " +
                                "JOIN FETCH o.member " +
                                "JOIN FETCH o.delievery", Order.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Order> findAllWithItem() {
        return em.createQuery(
                "SELECT distinct o " + // 조인에 따른 불필요한 order의 데이터 중복 방지
                        "FROM Order o " +
                        "JOIN FETCH o.member m " +
                        "JOIN FETCH o.delievery d " +
                        "JOIN FETCH o.orderItems oi " +
                        "JOIN FETCH oi.item i", Order.class).getResultList();
    }
}
