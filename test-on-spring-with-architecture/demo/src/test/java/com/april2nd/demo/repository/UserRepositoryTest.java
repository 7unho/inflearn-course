package com.april2nd.demo.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.april2nd.demo.model.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
//import org.springframework.test.context.TestPropertySource;

@DataJpaTest(showSql = true)
//@TestPropertySource("classpath:test-application.properties")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    @Test
    @DisplayName("UserRepository 연결 테스트")
    public void UserRepository_가_제대로_연결되었다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("rlawnsgh8395@naver.com");
        userEntity.setAddress("SEOUL");
        userEntity.setNickname("april2nd");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaa-aa-aa-aa-aaaaaa");

        //when
        UserEntity result = userRepository.save(userEntity);

        //then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    @DisplayName("ID와 유저상태로 유저 데이터를 찾아올 수 있다")
    public void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("rlawnsgh8395@naver.com");
        userEntity.setAddress("SEOUL");
        userEntity.setNickname("april2nd");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaa-aa-aa-aa-aaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("ID와 유저상태로 조회 시 데이터가 없다면 optional.empty를 내려준다")
    public void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("rlawnsgh8395@naver.com");
        userEntity.setAddress("SEOUL");
        userEntity.setNickname("april2nd");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaa-aa-aa-aa-aaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByIdAndStatus(1, UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("이메일과 유저상태로 유저 데이터를 찾아올 수 있다")
    public void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("rlawnsgh8395@naver.com");
        userEntity.setAddress("SEOUL");
        userEntity.setNickname("april2nd");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaa-aa-aa-aa-aaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("rlawnsgh8395@naver.com", UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("이메일과 유저상태로 조회 시 데이터가 없다면 optional.empty를 내려준다")
    public void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() throws Exception {
        //given
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("rlawnsgh8395@naver.com");
        userEntity.setAddress("SEOUL");
        userEntity.setNickname("april2nd");
        userEntity.setStatus(UserStatus.ACTIVE);
        userEntity.setCertificationCode("aaaaaa-aa-aa-aa-aaaaaa");

        //when
        userRepository.save(userEntity);
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("rlawnsgh8395@naver.com", UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }
}