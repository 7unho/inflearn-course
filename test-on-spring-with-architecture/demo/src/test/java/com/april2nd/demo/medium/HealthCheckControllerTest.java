package com.april2nd.demo.medium;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class HealthCheckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("헬스 체크 응답이 200으로 내려온다")
    public void 헬스_체크_응답이_200으로_내려_온다() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/health_check.html"))
                .andExpect(status().isOk());
    }
}