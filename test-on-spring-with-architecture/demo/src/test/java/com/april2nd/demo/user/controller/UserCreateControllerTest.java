package com.april2nd.demo.user.controller;

import com.april2nd.demo.mock.TestClockHolder;
import com.april2nd.demo.mock.TestContainer;
import com.april2nd.demo.mock.TestUuidHolder;
import com.april2nd.demo.user.controller.response.UserResponse;
import com.april2nd.demo.user.domain.UserCreate;
import com.april2nd.demo.user.domain.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class UserCreateControllerTest {
//    @Test
//    @DisplayName("사용자는 회원 가입을 할 수 있고, 회원 가입된 사용자는 PENDING 상태이다")
//    public void 사용자는_회원_가입을_할_수_있고_회원_가입된_사용자는_PENDING_상태이다() throws Exception {
//        // given
//        String nickname = "UnknownFlower";
//        String address = "jeju";
//        String email = "rlawnsgh8395@naver.com";
//        UserCreate userCreate = UserCreate.builder()
//                .nickname(nickname)
//                .address(address)
//                .email(email)
//                .build();
//        String requestBody = objectMapper.writeValueAsString(userCreate);
//        // when
//        // then
//        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
//        mockMvc.perform(
//                        post("/api/users")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(requestBody))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.email").value(email))
//                .andExpect(jsonPath("$.nickname").value(nickname))
//                .andExpect(jsonPath("$.status").value("PENDING"));
//    }

    @Test
    @DisplayName("사용자는 회원 가입을 할 수 있고, 회원 가입된 사용자는 PENDING 상태이다")
    public void 사용자는_회원_가입을_할_수_있고_회원_가입된_사용자는_PENDING_상태이다() throws Exception {
        // given
        String nickname = "UnknownFlower";
        String address = "jeju";
        String email = "rlawnsgh8395@naver.com";
        UserCreate userCreate = UserCreate.builder()
                .nickname(nickname)
                .address(address)
                .email(email)
                .build();

        TestContainer testContainer = TestContainer.builder()
                .uuidHolder(new TestUuidHolder("aaa-bb-cc"))
                .clockHolder(new TestClockHolder(970402L))
                .build();

        // when
        ResponseEntity<UserResponse> result = testContainer.userCreateController.create(userCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getNickname()).isEqualTo(nickname);
        assertThat(result.getBody().getEmail()).isEqualTo(email);
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.PENDING);

    }
}