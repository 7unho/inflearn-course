package com.april2nd.demo.mock;

import com.april2nd.demo.common.service.port.UuidHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestUuidHolder implements UuidHolder {
    private final String uuid;
    @Override
    public String random() {
        return uuid;
    }
}
