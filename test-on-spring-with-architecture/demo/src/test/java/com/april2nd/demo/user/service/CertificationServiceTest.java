package com.april2nd.demo.user.service;

import com.april2nd.demo.mock.FakeMailSender;
import com.april2nd.demo.user.service.port.MailSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CertificationServiceTest {
    @Test
    @DisplayName("")
    public void 이메일과_컨텐츠가_제대로_만들어져서_보내지는지_테스트한다() throws Exception {
        // given
        FakeMailSender fakeMailSender = new FakeMailSender();
        CertificationService certificationService = new CertificationService(fakeMailSender);
        String email = "rlawnsgh8395@naver.com";
        Long userId = 100L;
        String certificationCode = "aaaaa-aaaaaaaaaa-aaaaa-aaaaa";
        // when
        certificationService.send(email, userId, certificationCode);
        // then
        assertThat(fakeMailSender.email).isEqualTo(email);
        assertThat(fakeMailSender.title).isEqualTo("Please certify your email address");
        assertThat(fakeMailSender.content).isEqualTo("Please click the following link to certify your email address: http://localhost:8080/api/users/" + userId + "/verify?certificationCode=" + certificationCode);
    }
}