package com.april2nd.api.service;

import com.april2nd.api.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplyServiceTest {
    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;
    @Test
    public void 한번만응모() throws Exception {
        // given
        // when
        applyService.apply(1L);
        Long count = couponRepository.count();
        // then
        assertThat(count).isEqualTo(1L);
    }

    @Test
    public void 동시에_여러명_응모() throws Exception {
        // given
        int threadCount = 1000;

        // multi-thread 환경을 위한 변수
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // when
        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        long count = couponRepository.count();

        // then
        assertThat(count).isEqualTo(100L);
    }
}