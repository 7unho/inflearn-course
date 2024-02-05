package com.jpabook.jpashop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jpabook.jpashop.domain.OrderItem;
import lombok.Data;

@Data
public class OrderItemQueryDto {
    @JsonIgnore
    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(OrderItem orderItem) {
        this.orderId = orderItem.getOrder().getId();
        this.itemName = orderItem.getItem().getName();
        this.orderPrice = orderItem.getOrderPrice();
        this.count = orderItem.getCount();
    }

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
