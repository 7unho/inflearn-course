package com.april2nd.demo.service;

import com.april2nd.demo.exception.ResourceNotFoundException;
import com.april2nd.demo.repository.PostEntity;
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
                value = "/sql/post-service-test-data.sql",
                executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
        ),
        @Sql(
                value = "/sql/delete-all-data.sql",
                executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
        )
})
class PostServiceTest {
    @Autowired
    private PostService postService;

    @Test
    @DisplayName("postId로 포스트를 불러올 수 있다")
    public void postId로_포스트를_불러올_수_있다() throws Exception {
        //given
        Long postId = 1L;

        //when
        PostEntity result = postService.getPostById(postId);
        //then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    @DisplayName("postId로 조회할 수 없다면 예외를 던진다")
    public void postId로_조회할_수_없다면_예외를_던진다() throws Exception {
        //given
        //when
        //then
        assertThatThrownBy(() -> {
            postService.getPostById(111L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }
}