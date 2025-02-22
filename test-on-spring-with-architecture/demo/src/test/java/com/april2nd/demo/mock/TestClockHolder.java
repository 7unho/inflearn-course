package com.april2nd.demo.mock;

import com.april2nd.demo.common.service.port.ClockHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestClockHolder implements ClockHolder {
    private final long millis;
    @Override
    public long millis() {
        return millis;
    }
}
