package com.jpabook.jpashop.repository;

import com.jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null) {
            // item은 jpa에 저장하기 전 까지 id 값이 없다
            // -> 새로 생성한 객체이므로 em에 신규 등록
            em.persist(item);
        } else {
            // item값이 있다면, 기존에 이미 존재했던 객체
            // -> 업데이트 개념
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("SELECT i FROM Item i", Item.class)
                 .getResultList();
    }
}
