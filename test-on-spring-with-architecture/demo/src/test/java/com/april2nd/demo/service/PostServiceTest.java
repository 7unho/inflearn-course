package com.april2nd.demo.service;

import com.april2nd.demo.exception.ResourceNotFoundException;
import com.april2nd.demo.model.dto.PostCreateDto;
import com.april2nd.demo.model.dto.PostUpdateDto;
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
        Long postId = 100L;

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

    @Test
    @DisplayName("PostCrateDto로 새로운 포스트를 생성할 수 있다")
    public void PostCrateDto로_새로운_포스트를_생성할_수_있다() throws Exception {
        //given
        PostCreateDto postCreateDto = PostCreateDto.builder()
                .content("createdByPostCreateDto")
                .writerId(100L)
                .build();
        //when
        PostEntity result = postService.create(postCreateDto);

        //then
        assertThat(result.getWriter().getId()).isEqualTo(100L);

        // TODO: 테스트 가능한 설계로 변경하기 ( PostServiceTest.create )
        assertThat(result.getCreatedAt()).isGreaterThan(0L);
        //assertThat(result.getCreatedAt()).isEqualTo(0L);
    }

    @Test
    @DisplayName("PostUpdateDto로 포스트를 수정할 수 있다")
    public void PostUpdateDto로_포스트를_수정할_수_있다() throws Exception {
        //given
        PostUpdateDto postUpdateDto = PostUpdateDto.builder()
                .content("UpdatedPostByPostUpdateDto")
                .build();
        //when
        PostEntity result = postService.update(100L, postUpdateDto);

        //then
        assertThat(result.getContent()).isEqualTo(postUpdateDto.getContent());
        // TODO: 테스트 가능한 설계로 변경하기 ( PostServiceTest.update )
        assertThat(result.getModifiedAt()).isGreaterThan(0L);
        //assertThat(result.getModifiedAt()).isGreaterThan(0L);
    }
}