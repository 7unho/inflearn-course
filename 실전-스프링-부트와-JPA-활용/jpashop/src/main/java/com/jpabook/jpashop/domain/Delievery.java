package com.jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter@Setter
public class Delievery {
    @Id
    @GeneratedValue
    @Column(name = "delievery_id")
    private Long id;

    @OneToOne(mappedBy = "delievery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private DelieveryStatus status; // READY, COMP
}
