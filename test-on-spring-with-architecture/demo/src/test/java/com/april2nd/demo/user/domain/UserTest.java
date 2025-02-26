package com.april2nd.demo.user.domain;

import com.april2nd.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.april2nd.demo.mock.TestClockHolder;
import com.april2nd.demo.mock.TestUuidHolder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserTest {
    @Test
    @DisplayName("")
    public void UserCreate_객체로_생성할_수_있다() throws Exception {
        // given
//        values (200, 'user@pending.com', 'april2nd_pending', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'PENDING', 0);
        String email = "user@pending.com";
        String nickname = "april2nd_pending";
        String address = "seoul";
        String certificationCode = "aaaaa-aaaaaaaaaa-aaaaa-aaaaa";

        UserCreate userCreate = UserCreate.builder()
                .email(email)
                .nickname(nickname)
                .address(address)
                .build();
        // when
        User user = User.from(userCreate, new TestUuidHolder(certificationCode));

        // then
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getAddress()).isEqualTo(address);
        assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        assertThat(user.getCertificationCode()).isEqualTo(certificationCode);
    }

    @Test
    @DisplayName("")
    public void UserUpdate_객체로_데이터를_업데이트_할_수_있다() throws Exception {
        // given
//        100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0
        String nickname = "Updated Nickname";
        String address = "Updated Address";
        User user = User.builder()
                .id(100L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .build();

        UserUpdate userUpdate = UserUpdate.builder()
                .nickname(nickname)
                .address(address)
                .build();
        // when
        user = user.update(userUpdate);

        // then
        assertThat(user.getId()).isEqualTo(100L);
        assertThat(user.getEmail()).isEqualTo("rlawnsgh8395@naver.com");
        assertThat(user.getNickname()).isEqualTo(nickname);
        assertThat(user.getAddress()).isEqualTo(address);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(user.getLastLoginAt()).isEqualTo(100L);
        assertThat(user.getCertificationCode()).isEqualTo("aaaaa-aaaaaaaaaa-aaaaa-aaaaa");
    }

    @Test
    @DisplayName("")
    public void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() throws Exception {
        // given
        Long loginMillis = 970402L;
        User user = User.builder()
                .id(100L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(100L)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .build();

        // when
        user = user.login(new TestClockHolder(loginMillis));

        // then
        assertThat(user.getLastLoginAt()).isEqualTo(loginMillis);
    }

    @Test
    @DisplayName("")
    public void 유효한_인증_코드로_계정을_활성화_할_수_있다() throws Exception {
        // given
        String certificationCode = "aaaaa-aaaaaaaaaa-aaaaa-aaaaa";
        User user = User.builder()
                .id(100L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode(certificationCode)
                .build();
        // when
        user = user.certificate(certificationCode);
        // then
        assertThat(user.getCertificationCode()).isEqualTo(certificationCode);
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("")
    public void 잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() throws Exception {
        // given
        String invalidCertificationCode = "INVALID CERTIFICATION CODE";
        User user = User.builder()
                .id(100L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .status(UserStatus.PENDING)
                .lastLoginAt(100L)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .build();
        // when
        // then
        assertThatThrownBy(() -> user.certificate(invalidCertificationCode)).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}