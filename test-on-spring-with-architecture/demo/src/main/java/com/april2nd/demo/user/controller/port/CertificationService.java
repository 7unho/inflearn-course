package com.april2nd.demo.user.controller.port;

public interface CertificationService {
    void send(String email, long userId, String certificationCode);
}
