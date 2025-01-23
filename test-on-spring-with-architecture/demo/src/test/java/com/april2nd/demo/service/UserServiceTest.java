package com.april2nd.demo.service;

import com.april2nd.demo.exception.ResourceNotFoundException;
import com.april2nd.demo.repository.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
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
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Test
    @DisplayName("getByEmail은 ACTIVE 상태의 유저를 찾아올 수 있다.")
    public void getByEmail_은_ACTIVE_상태인_유저를_찾아올_수_있다() throws Exception {
        //given
        String email = "rlawnsgh8395@naver.com";

        //when
        UserEntity result = userService.getByEmail(email);

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
        Long id = 1l;

        //when
        UserEntity result = userService.getById(id);

        //then
        assertThat(result.getNickname()).isEqualTo("april2nd");
    }

    @Test
    @DisplayName("getById는 PENDING 상태의 유저를 찾아올 수 없다.")
    public void getById_는_PENDING_상태인_유저를_찾아올_수_없다() throws Exception {
        //given
        Long id = 2l;

        //when
        //then
        assertThatThrownBy(() -> {
            userService.getById(id);
        }).isInstanceOf(ResourceNotFoundException.class);
    }
}