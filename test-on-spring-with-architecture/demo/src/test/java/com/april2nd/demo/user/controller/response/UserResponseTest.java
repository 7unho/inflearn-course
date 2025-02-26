package com.april2nd.demo.user.controller.response;

import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {
    // TODO: User로_응답(UserResponse)을_생성할_수_있다
    @Test
    public void User로_응답을_생성할_수_있다() throws Exception {
        // given
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
        UserResponse userResponse = UserResponse.from(user);
        // then
        assertThat(userResponse.getId()).isEqualTo(100L);
        assertThat(userResponse.getEmail()).isEqualTo("rlawnsgh8395@naver.com");
        assertThat(userResponse.getNickname()).isEqualTo("april2nd");
        assertThat(userResponse.getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(userResponse.getLastLoginAt()).isEqualTo(100L);
    }
}