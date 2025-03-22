package com.april2nd.consumer.consumer;

import com.april2nd.consumer.domain.Coupon;
import com.april2nd.consumer.repository.CouponRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CouponCreateConsumer {
    private final CouponRepository couponRepository;

    public CouponCreateConsumer(CouponRepository couponRepository) {
        this.couponRepository = couponRepository;
    }

    @KafkaListener(
            topics = "coupon_create",
            groupId = "group_1"
    )
    public void listener(Long userId) {
        couponRepository.save(new Coupon(userId));
    }
}
