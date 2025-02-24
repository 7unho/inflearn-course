package com.april2nd.demo.user.service;

import com.april2nd.demo.common.domain.exception.CertificationCodeNotMatchedException;
import com.april2nd.demo.common.domain.exception.ResourceNotFoundException;
import com.april2nd.demo.mock.FakeMailSender;
import com.april2nd.demo.mock.FakeUserRepository;
import com.april2nd.demo.mock.TestClockHolder;
import com.april2nd.demo.mock.TestUuidHolder;
import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserCreate;
import com.april2nd.demo.user.domain.UserStatus;
import com.april2nd.demo.user.domain.UserUpdate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
class UserServiceTest {
    private UserService userService;

    @BeforeEach
    void init() {
        /*
        (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
        (200, 'user@pending.com', 'april2nd_pending', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'PENDING', 0);
         */
        FakeMailSender fakeMailSender = new FakeMailSender();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        User activeUserForTest = User.builder()
                .id(100L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(970402L)
                .build();

        User pendingUserForTest = User.builder()
                .id(200L)
                .email("user@pending.com")
                .nickname("april2nd_pending")
                .address("seoul")
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .status(UserStatus.PENDING)
                .lastLoginAt(970402L)
                .build();

        this.userService = UserService.builder()
                .certificationService(new CertificationService(fakeMailSender))
                .clockHolder(new TestClockHolder(970402L))
                .userRepository(fakeUserRepository)
                .uuidHolder(new TestUuidHolder("aaaaa-aaaaaaaaaa-aaaaa-aaaaa"))
                .build();

        fakeUserRepository.save(activeUserForTest);
        fakeUserRepository.save(pendingUserForTest);
    }

    @Test
    @DisplayName("getByEmail은 ACTIVE 상태의 유저를 찾아올 수 있다.")
    public void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        String email = "rlawnsgh8395@naver.com";

        //when
        User result = userService.getByEmail(email);

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
            userService.getByEmail(email);
        }).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("getById는 ACTIVE 상태의 유저를 찾아올 수 있다.")
    public void getById_는_ACTIVE_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        Long id = 100L;

        //when
        User result = userService.getById(id);

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
            userService.getById(id);
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
        ///BDDMockito.doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        //when
        User result = userService.create(userCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getStatus()).isEqualTo(UserStatus.PENDING);
        // TODO 테스트 가능한 설계로 변경하기
        assertThat(result.getCertificationCode()).isEqualTo("aaaaa-aaaaaaaaaa-aaaaa-aaaaa");
    }

    @Test
    @DisplayName("userCrateDto를 이용하여 유저 정보를 수정할 수 있다")
    public void userCrateDto_를_이용하여_유저_정보_를_수정할_수_있다() throws Exception {
        //given
        UserUpdate userUpdateDto = UserUpdate.builder()
                .address("incheon")
                .nickname("unknownflower")
                .build();

        Long userId = userService.getByEmail("rlawnsgh8395@naver.com").getId();

        //when
        User result = userService.update(userId, userUpdateDto);

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
        userService.login(100L);

        //then
        User result = userService.getById(100L);
        // TODO 테스트 가능한 설계로 변경하기
         assertThat(result.getLastLoginAt()).isEqualTo(970402L);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 인증 코드로 활성화시킬 수 있다")
    public void PENDING_상태의_사용자는_인증_코드로_활성화시킬_수_있다() throws Exception {
        //given
        //when
        userService.verifyEmail(200L, "aaaaa-aaaaaaaaaa-aaaaa-aaaaa");

        //then
        User result = userService.getById(200L);
        assertThat(result.getStatus()).isEqualTo(UserStatus.ACTIVE);
    }

    @Test
    @DisplayName("PENDING 상태의 사용자는 잘못된 인증 코드를 받으면 에러를 던진다")
    public void PENDING_상태의_사용자는_잘못된_인증_코드를_받으면_에러를_던진다() throws Exception {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            userService.verifyEmail(200L, "Invalid-certification-code");
        }).isInstanceOf(CertificationCodeNotMatchedException.class);
    }
}