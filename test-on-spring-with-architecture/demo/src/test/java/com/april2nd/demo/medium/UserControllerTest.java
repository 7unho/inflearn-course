package com.april2nd.demo.medium;

import com.april2nd.demo.user.domain.UserStatus;
import com.april2nd.demo.user.domain.UserUpdate;
import com.april2nd.demo.user.infrastructure.UserEntity;
import com.april2nd.demo.user.infrastructure.UserJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@SqlGroup({
        @Sql(
                value = "/sql/post-service-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        ),
        @Sql(
                value = "/sql/delete-all-data.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
})
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("사용자는 특정 유저의 개인 정보는 소거된 정보를 전달 뱓을 수 있다")
    public void 사용자는_특정_유저의_개인_정보는_소거된_정보를_전달_뱓을_수_있다() throws Exception {
        //given
        //values (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
        //when
        //then
        mockMvc.perform(get("/api/users/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.email").value("rlawnsgh8395@naver.com"))
                .andExpect(jsonPath("$.nickname").value("april2nd"))
                .andExpect(jsonPath("$.address").doesNotExist())
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    //TODO: 존재하지 않는 유저의 ID로 API 호출할 경우 404 응답을 받는다.
    @Test
    @DisplayName("존재하지 않는 유저의 ID로 API 호출할 경우 404 응답을 받는다")
    public void 존재하지_않는_유저의_ID로_API_호출할_경우_404_응답을_받는다() throws Exception {
        //given
        //values (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
        //when
        //then
        mockMvc.perform(get("/api/users/404"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Users에서 ID 404를 찾을 수 없습니다."));
    }

    //TODO: 사용자는 인증 코드로 계정을 활성화 시킬 수 있다
    @Test
    @DisplayName("사용자는 인증 코드로 계정을 활성화 시킬 수 있다.")
    public void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception {
        //given
        Long id = 200L;
        String certificationCode = "aaaaa-aaaaaaaaaa-aaaaa-aaaaa";
        /*
        (200, 'user@pending.com', 'april2nd_pending', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'PENDING', 0);
         */
        //when
        mockMvc.perform(get("/api/users/{id}/verify", id)
                        .queryParam("certificationCode", certificationCode))
                .andExpect(status().isFound());
        //then
        UserEntity userEntity = userJpaRepository.findById(id).get();
        assertThat(userEntity.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    //TODO: 사용자는 내 정보를 불러올 때 개인정보인 주소도 갖고 올 수 있다.
    //
    /*
    /api/users/me (header:email)

    [ 프로세스 ]
    1. user를 찾을 수 없다면 예외 호출
    2. lastlogin시각 업데이트
    3. 상태코드 200 및 id, email, nickname, status, address, lastloginat 리턴
     */
    @Test
    @DisplayName("사용자는 내 정보를 불러올 때 개인정보인 주소도 갖고 올 수 있다.")
    public void 사용자는_내_정보를_불러올_때_개인정보인_주소도_갖고_올_수_있다() throws Exception {
        // given
        String userEmail = "rlawnsgh8395@naver.com";
        // when
        // then
        mockMvc.perform(
                        get("/api/users/me").header("EMAIL", userEmail))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.email").value("rlawnsgh8395@naver.com"))
                .andExpect(jsonPath("$.nickname").value("april2nd"))
                .andExpect(jsonPath("$.address").value("seoul"))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    @DisplayName("사용자는 내 정보를 수정할 수 있다")
    public void 사용자는_내_정보를_수정할_수_있다() throws Exception {
        /*
        requestheader = email
        requestBody = nickname, address
        response = id, email, nickname, address, status
         */
        // given
        String nickname = "UPDATED nickname";
        String address = "UPDATED address";
        String email = "rlawnsgh8395@naver.com";

        UserUpdate userUpdateDto = UserUpdate.builder()
                .nickname(nickname)
                .address(address)
                .build();

        String requestBody = objectMapper.writeValueAsString(userUpdateDto);
        // when
        // then
        mockMvc.perform(
                        put("/api/users/me")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("EMAIL", email)
                                .content(requestBody)
                ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.email").value("rlawnsgh8395@naver.com"))
                .andExpect(jsonPath("$.nickname").value(nickname))
                .andExpect(jsonPath("$.address").value(address))
                .andExpect(jsonPath("$.status").value("ACTIVE"));

    }

    @Test
    @DisplayName("")
    public void 사용자의_인증_코드가_일치하지_않을_경우_권한_없음_에러를_내려준다() throws Exception {
        // given
        // when
        // then
        mockMvc.perform(
                        get("/api/users/{id}/verify", 100L)
                                .queryParam("certificationCode", "Invalid Certification Code"))
                .andExpect(status().isForbidden());
    }
}