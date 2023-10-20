package com.jpabook.jpashop.domain;

import com.jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity @Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
    @Id
    @GeneratedValue @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    /*


    왜 protected? -> JPA는 기본생성자를 protected까지 허용해준다.
    protected OrderItem() {}
    ==> @NoArgsConstructor(access = AccessLevel.PROTECTED) 로 대체 가능
    */

    //== 생성 메서드 ==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        // OrderItem을 생성하면서, 주문 수량(count) 만큼 재고 감소
        item.removeStock(count);
        return orderItem;
    }

    //== 비즈니스 로직 ==/
    public void cancel() {
        getItem().addStock(count);
    }

    //== 조회 로직 ==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
