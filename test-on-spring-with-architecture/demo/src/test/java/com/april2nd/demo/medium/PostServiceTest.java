package com.april2nd.demo.medium;

import com.april2nd.demo.common.domain.exception.ResourceNotFoundException;
import com.april2nd.demo.post.domain.Post;
import com.april2nd.demo.post.domain.PostCreate;
import com.april2nd.demo.post.domain.PostUpdate;
import com.april2nd.demo.post.service.PostService;
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
        //when
        Post result = postService.getPostById(100L);
        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("helloworld");
        assertThat(result.getWriter().getEmail()).isEqualTo("rlawnsgh8395@naver.com");
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
        PostCreate postCreateDto = PostCreate.builder()
                .content("createdByPostCreateDto")
                .writerId(100L)
                .build();
        //when
        Post result = postService.create(postCreateDto);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getContent()).isEqualTo("createdByPostCreateDto");

        // TODO: 테스트 가능한 설계로 변경하기 ( PostServiceTest.create )
        assertThat(result.getCreatedAt()).isGreaterThan(0L);
        //assertThat(result.getCreatedAt()).isEqualTo(0L);
    }

    @Test
    @DisplayName("PostUpdateDto로 포스트를 수정할 수 있다")
    public void PostUpdateDto로_포스트를_수정할_수_있다() throws Exception {
        //given
        PostUpdate postUpdateDto = PostUpdate.builder()
                .content("UpdatedPostByPostUpdateDto")
                .build();
        //when
        postService.update(100L, postUpdateDto);

        //then
        Post result = postService.getPostById(100L);
        assertThat(result.getContent()).isEqualTo(postUpdateDto.getContent());
        // TODO: 테스트 가능한 설계로 변경하기 ( PostServiceTest.update )
        assertThat(result.getModifiedAt()).isGreaterThan(0L);
        //assertThat(result.getModifiedAt()).isGreaterThan(0L);
    }
}