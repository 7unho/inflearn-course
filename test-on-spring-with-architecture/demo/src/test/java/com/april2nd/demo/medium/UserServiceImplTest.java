package com.april2nd.demo.medium;

import com.april2nd.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.april2nd.demo.common.domain.exception.ResourceNotFoundException;
import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserCreate;
import com.april2nd.demo.user.domain.UserStatus;
import com.april2nd.demo.user.domain.UserUpdate;
import com.april2nd.demo.user.service.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest()
@TestPropertySource("classpath:test-application.properties")
@SqlGroup({
        @Sql(
                value = "/sql/user-repository-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        ),
        @Sql(
                value = "/sql/delete-all-data.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
})
class UserServiceImplTest {
    @Autowired
    private UserServiceImpl userServiceImpl;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    @DisplayName("getByEmail은 ACTIVE 상태의 유저를 찾아올 수 있다.")
    public void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        String email = "rlawnsgh8395@naver.com";

        //when
        User result = userServiceImpl.getByEmail(email);

        //then
        assertThat(result.getNickname()).isEqualTo("april2nd");
    }

    @Test
    @DisplayName("getByEmail은 PENDING 상태의 유저를 찾아올 수 없다.")
    public void getByEmail_은_PENDING_상태인_유저를_찾아올_수_없다() throws Exception {
        //given
        String email = "user@pending.com";

        //when
        //then
        assertThatThrownBy(() -> {
            userServiceImpl.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getById는 ACTIVE 상태의 유저를 찾아올 수 있다.")
    public void getById_는_ACTIVE_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        Long id = 100L;

        //when
        User result = userServiceImpl.getById(id);

        //then
        assertThat(result.getNickname()).isEqualTo("april2nd");
    }

    @Test
    @DisplayName("getById는 PENDING 상태의 유저를 찾아올 수 없다.")
    public void getById_는_PENDING_상태인_유저를_찾아올_수_없다() throws Exception {
        //given
        Long id = 200L;

        //when
        //then
        assertThatThrownBy(() -> {
            userServiceImpl.getById(id);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("userCrateDto를 이용하여 유저를 생성할 수 있다")
    public void userCrateDto_를_이용하여_유저를_생성할_수_있다() throws Exception {
        //given
        UserCreate userCreateDto = UserCreate.builder()
                .email("createdByPending@test.com")
                .nickname("april2nd")
                .address("seoul")
                .build();
        BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        User result = userServiceImpl.create(userCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        // TODO 테스트 가능한 설계로 변경하기
//        assertThat(result.getCertificationCode()).isEqualTo("");
    }

    @Test
    @DisplayName("userCrateDto를 이용하여 유저 정보를 수정할 수 있다")
    public void userCrateDto_를_이용하여_유저_정보_를_수정할_수_있다() throws Exception {
        //given
        UserUpdate userUpdateDto = UserUpdate.builder()
                .address("incheon")
                .nickname("unknownflower")
                .build();

        Long userId = userServiceImpl.getByEmail("rlawnsgh8395@naver.com").getId();

        //when
        User result = userServiceImpl.update(userId, userUpdateDto);

        //then
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getAddress()).isEqualTo("incheon");
        assertThat(result.getNickname()).isEqualTo("unknownflower");
    }

    @Test
    @DisplayName("user를 로그인 시키면 마지막 로그인 시간이 변경된다")
    public void user_를_로그인_시키면_마지막_로그인_시간이_변경된다() throws Exception {
        //given
        //when
        userServiceImpl.login(100L);

        //then
        User result = userServiceImpl.getById(100L);
        // TODO 테스트 가능한 설계로 변경하기
        // assertThat(result.getLastLoginAt()).isEqualTo(로그인 한 시각);
        assertThat(result.getLastLoginAt()).isGreaterThan(0L);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 인증 코드로 활성화시킬 수 있다")
    public void PENDING_상태의_사용자는_인증_코드로_활성화시킬_수_있다() throws Exception {
        //given
        //when
        userServiceImpl.verifyEmail(200L, "aaaaa-aaaaaaaaaa-aaaaa-aaaaa");

        //then
        User result = userServiceImpl.getById(200L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 잘못된 인증 코드를 받으면 에러를 던진다")
    public void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() throws Exception {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userServiceImpl.verifyEmail(200L, "Invalid-certification-code");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}