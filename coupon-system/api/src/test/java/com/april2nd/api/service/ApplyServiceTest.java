package com.april2nd.api.service;

import com.april2nd.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}