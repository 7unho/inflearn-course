package com.april2nd.api.service;

import com.april2nd.api.producer.CouponCreateProducer;
import com.april2nd.api.repository.AppliedUserRepository;
import com.april2nd.api.repository.CouponCountRepository;
import com.april2nd.api.repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplyService {
    private final CouponCountRepository couponCountRepository;
    private final CouponCreateProducer couponCreateProducer;
    private final AppliedUserRepository appliedUserRepository;

    public ApplyService(
            CouponCountRepository couponCountRepository,
            CouponCreateProducer couponCreateProducer,
            AppliedUserRepository appliedUserRepository) {
        this.couponCountRepository = couponCountRepository;
        this.couponCreateProducer = couponCreateProducer;
        this.appliedUserRepository = appliedUserRepository;
    }

    public void apply(Long userId) {
        Long apply = appliedUserRepository.add(userId);
        if (apply != 1) return; // 이미 발급했다면

        long count = couponCountRepository.increment();
        if (count > 100) return; // 수량이 초과됐다면

        couponCreateProducer.create(userId);
    }
}