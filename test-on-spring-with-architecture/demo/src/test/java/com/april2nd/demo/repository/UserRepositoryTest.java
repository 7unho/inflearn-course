package com.april2nd.demo.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.april2nd.demo.model.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
//import org.springframework.test.context.TestPropertySource;

@DataJpaTest(showSql = true)
@TestPropertySource("classpath:test-application.properties")
@Sql("classpath:/sql/user-repository-test-data.sql")
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("ID와 유저상태로 유저 데이터를 찾아올 수 있다")
    public void findByIdAndStatus_로_유저_데이터를_찾아올_수_있다() throws Exception {
        //given

        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(100L, UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("ID와 유저상태로 조회 시 데이터가 없다면 optional.empty를 내려준다")
    public void findByIdAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() throws Exception {
        //given

        //when
        Optional<UserEntity> result = userRepository.findByIdAndStatus(100L, UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("이메일과 유저상태로 유저 데이터를 찾아올 수 있다")
    public void findByEmailAndStatus_로_유저_데이터를_찾아올_수_있다() throws Exception {
        //given

        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("rlawnsgh8395@naver.com", UserStatus.ACTIVE);

        //then
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @DisplayName("이메일과 유저상태로 조회 시 데이터가 없다면 optional.empty를 내려준다")
    public void findByEmailAndStatus_는_데이터가_없으면_Optional_empty_를_내려준다() throws Exception {
        //given

        //when
        Optional<UserEntity> result = userRepository.findByEmailAndStatus("rlawnsgh8395@naver.com", UserStatus.PENDING);

        //then
        assertThat(result.isEmpty()).isTrue();
    }
}