package com.april2nd.demo.user.service.port;

public interface MailSender {
    void send(String email, String title, String content);
}
