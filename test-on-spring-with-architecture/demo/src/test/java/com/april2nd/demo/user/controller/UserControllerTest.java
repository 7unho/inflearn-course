package com.april2nd.demo.user.controller;

import com.april2nd.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.april2nd.demo.common.domain.exception.ResourceNotFoundException;
import com.april2nd.demo.mock.TestClockHolder;
import com.april2nd.demo.mock.TestContainer;
import com.april2nd.demo.mock.TestUuidHolder;
import com.april2nd.demo.user.controller.port.UserReadService;
import com.april2nd.demo.user.controller.response.MyProfileResponse;
import com.april2nd.demo.user.controller.response.UserResponse;
import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserCreate;
import com.april2nd.demo.user.domain.UserStatus;
import com.april2nd.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserControllerTest {
    @Test
    @DisplayName("사용자는 특정 유저의 개인 정보는 소거된 정보를 전달 뱓을 수 있다")
    public void 사용자는_특정_유저의_개인_정보는_소거된_정보를_전달_뱓을_수_있다() throws Exception {
        //values (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
        //given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(970402L))
                .uuidHolder(new TestUuidHolder("aaaaa-aaaaaaaaaa-aaaaa-aaaaa"))
                .build();

        testContainer.userRepository.save(User.builder()
                .id(100L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(970402L)
                .build());
        //when
        ResponseEntity<UserResponse> result = testContainer.userController.getUserById(100L);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(100L);
        assertThat(result.getBody().getEmail()).isEqualTo("rlawnsgh8395@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("april2nd");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(970402L);
    }

    @Test
    @DisplayName("사용자는 특정 유저의 개인 정보는 소거된 정보를 전달 뱓을 수 있다")
    public void 사용자는_특정_유저의_개인_정보는_소거된_정보를_전달_뱓을_수_있다_with_Stub() throws Exception {
        //values (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
        //given
        UserController userController = UserController.builder()
                .userReadService(new UserReadService() {
                    @Override
                    public User getByEmail(String email) {
                        return null;
                    }

                    @Override
                    public User getById(long id) {
                        return User.builder()
                                .id(100L)
                                .email("rlawnsgh8395@naver.com")
                                .nickname("april2nd")
                                .status(UserStatus.ACTIVE)
                                .lastLoginAt(970402L)
                                .build();
                    }
                })
                .build();
        //when
        ResponseEntity<UserResponse> result = userController.getUserById(100L);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody()).isNotNull();
        assertThat(result.getBody().getId()).isEqualTo(100L);
        assertThat(result.getBody().getEmail()).isEqualTo("rlawnsgh8395@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("april2nd");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(970402L);
    }

    //TODO: 존재하지 않는 유저의 ID로 API 호출할 경우 404 응답을 받는다.
    @Test
    @DisplayName("존재하지 않는 유저의 ID로 API 호출할 경우 404 응답을 받는다")
    public void 존재하지_않는_유저의_ID로_API_호출할_경우_404_응답을_받는다_Stub() throws Exception {
        //values (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
        //given
        UserController userController = UserController.builder()
                .userReadService(new UserReadService() {
                    @Override
                    public User getByEmail(String email) {
                        return null;
                    }

                    @Override
                    public User getById(long id) {
                        throw new ResourceNotFoundException("Users", id);
                    }
                })
                .build();
        //when
        //then
        assertThatThrownBy(() -> {
            userController.getUserById(404L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    //TODO: 존재하지 않는 유저의 ID로 API 호출할 경우 404 응답을 받는다.
    @Test
    @DisplayName("존재하지 않는 유저의 ID로 API 호출할 경우 404 응답을 받는다")
    public void 존재하지_않는_유저의_ID로_API_호출할_경우_404_응답을_받는다() throws Exception {
        //values (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();

        //when
        //then
        assertThatThrownBy(() -> {
            testContainer.userController.getUserById(404L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    //TODO: 사용자는 인증 코드로 계정을 활성화 시킬 수 있다
    @Test
    @DisplayName("사용자는 인증 코드로 계정을 활성화 시킬 수 있다.")
    public void 사용자는_인증_코드로_계정을_활성화_시킬_수_있다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        /*
        (200, 'user@pending.com', 'april2nd_pending', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'PENDING', 0);
         */
        testContainer.userRepository.save(User.builder()
                .id(200L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .lastLoginAt(970402L)
                .build());

        //when
        ResponseEntity<Void> result = testContainer.userController.verifyEmail(200L, "aaaaa-aaaaaaaaaa-aaaaa-aaaaa");

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        assertThat(testContainer.userReadService.getById(200L).getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    //TODO: 사용자는 인증 코드로 계정을 활성화 시킬 수 있다
    @Test
    public void 사용자는_인증_코드가_일치하지_않을_경유_권한_없음_에러를_받는다() throws Exception {
        //given
        TestContainer testContainer = TestContainer.builder()
                .build();
        /*
        (200, 'user@pending.com', 'april2nd_pending', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'PENDING', 0);
         */
        testContainer.userRepository.save(User.builder()
                .id(200L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .status(UserStatus.PENDING)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .lastLoginAt(970402L)
                .build());

        //when
        //then
        assertThatThrownBy(() -> {
            testContainer.userController.verifyEmail(200L, "INVALID_CERTIFICATION_CODE");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
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
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(12345678L))
                .build();

        testContainer.userRepository.save(User.builder()
                .id(200L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .lastLoginAt(970402L)
                .build());
        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.getMyInfo("rlawnsgh8395@naver.com");

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getId()).isEqualTo(200L);
        assertThat(result.getBody().getEmail()).isEqualTo("rlawnsgh8395@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("april2nd");
        assertThat(result.getBody().getAddress()).isEqualTo("seoul");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(12345678L);
    }

    @Test
    @DisplayName("사용자는 내 정보를 수정할 수 있다")
    public void 사용자는_내_정보를_수정할_수_있다() {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(new TestClockHolder(12345678L))
                .build();

        testContainer.userRepository.save(User.builder()
                .id(200L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .lastLoginAt(970402L)
                .build());

        UserUpdate userUpdate = UserUpdate.builder()
                .address("incheon")
                .nickname("unknownflower")
                .build();

        // when
        ResponseEntity<MyProfileResponse> result = testContainer.userController.updateMyInfo("rlawnsgh8395@naver.com", userUpdate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(result.getBody().getId()).isEqualTo(200L);
        assertThat(result.getBody().getEmail()).isEqualTo("rlawnsgh8395@naver.com");
        assertThat(result.getBody().getNickname()).isEqualTo("unknownflower");
        assertThat(result.getBody().getAddress()).isEqualTo("incheon");
        assertThat(result.getBody().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(result.getBody().getLastLoginAt()).isEqualTo(970402L);
    }
}