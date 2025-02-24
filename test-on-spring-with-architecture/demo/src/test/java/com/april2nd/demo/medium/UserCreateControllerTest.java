package com.april2nd.demo.medium;

import com.april2nd.demo.user.domain.UserCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@Sql(
        value = "/sql/delete-all-data.sql",
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
class UserCreateControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JavaMailSender mailSender;

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
        String requestBody = objectMapper.writeValueAsString(userCreate);
        // when
        // then
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        mockMvc.perform(
                        post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
}