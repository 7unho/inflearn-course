package com.april2nd.demo.user.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("")
    public void UserCreate_객체로_생성할_수_있다() throws Exception {
        // given
        // when
        // then

    }

    @Test
    @DisplayName("")
    public void UserUpdate_객체로_데이터를_업데이트_할_수_있다() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("")
    public void 로그인을_할_수_있고_로그인시_마지막_로그인_시간이_변경된다() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("")
    public void 유효한_인증_코드로_계정을_활성화_할_수_있다() throws Exception {
        // given

        // when

        // then

    }

    @Test
    @DisplayName("")
    public void 잘못된_인증_코드로_계정을_활성화_하려하면_에러를_던진다() throws Exception {
        // given

        // when

        // then

    }
}