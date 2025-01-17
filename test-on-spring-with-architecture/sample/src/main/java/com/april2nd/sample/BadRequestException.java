package com.april2nd.sample;

public class BadRequestException extends RuntimeException {
    public BadRequestException() {
        super("유효하지 않은 입력 길이 입니다.");
    }
}
