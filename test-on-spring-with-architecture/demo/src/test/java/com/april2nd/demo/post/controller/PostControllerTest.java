package com.april2nd.demo.post.controller;

import com.april2nd.demo.common.domain.exception.ResourceNotFoundException;
import com.april2nd.demo.mock.TestContainer;
import com.april2nd.demo.post.controller.response.PostResponse;
import com.april2nd.demo.post.domain.Post;
import com.april2nd.demo.post.domain.PostUpdate;
import com.april2nd.demo.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PostControllerTest {
    private TestContainer testContainer;

    /*
        PostResponse = id, content, writer

        test post (100, 'helloworld', 1678530673958, 0, 100);
     */
    @BeforeEach
    public void init() {
        this.testContainer = TestContainer.builder()
                .clockHolder(() -> 970402L)
                .build();

        User user = User.builder()
                .id(100L)
                .build();

        Post post = Post.builder()
                .id(101L)
                .content("helloworld")
                .writer(user)
                .build();

        testContainer.userRepository.save(user);
        testContainer.postRepository.save(post);
    }

    // TODO: 포스트 ID를 통해 특정 게시물 정보를 불러올 수 있다
//    @Test
//    @DisplayName("포스트 ID를 통해 특정 게시물 정보를 불러올 수 있다")
//    public void 포스트_ID를_통해_특정_게시물_정보를_불러올_수_있다_mockMVC() throws Exception {
//        // given
//        Long postId = 100L;
//        // when
//
//        // then
//        mockMvc.perform(get("/api/posts/{id}", postId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.content").value("helloworld"))
//                .andExpect(jsonPath("$.writer.id").isNumber())
//                .andExpect(jsonPath("$.writer.email").value("rlawnsgh8395@naver.com"))
//                .andExpect(jsonPath("$.writer.nickname").value("april2nd"))
//                .andExpect(jsonPath("$.writer.status").value("ACTIVE"));
//    }

    @Test
    @DisplayName("포스트 ID를 통해 특정 게시물 정보를 불러올 수 있다")
    public void 포스트_ID를_통해_특정_게시물_정보를_불러올_수_있다() throws Exception {
        // given
        // when
        ResponseEntity<PostResponse> result = testContainer.postController.getById(101L);
        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(101L);
        assertThat(result.getBody().getContent()).isEqualTo("helloworld");
        assertThat(result.getBody().getWriter().getId()).isEqualTo(100L);
    }

    // TODO: 포스트 ID를 통해 특정 게시물 정보를 수정할 수 있다.
//    @Test
//    @DisplayName("포스트의_포스트_ID를_통해_특정_게시물_정보를_수정할_수_있다")
//    public void 포스트의_포스트_ID를_통해_특정_게시물_정보를_수정할_수_있다_mockMVC() throws Exception {
//        // given
//        String content = "UPDATED content";
//        Long postId = 100L;
//
//        PostUpdate postUpdateDto = PostUpdate.builder()
//                .content(content)
//                .build();
//
//        String requestBody = objectMapper.writeValueAsString(postUpdateDto);
//
//        // when
//        // then
//        mockMvc.perform(
//                put("/api/posts/{id}", postId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.content").value(content));
//    }

    @Test
    @DisplayName("포스트의_포스트_ID를_통해_특정_게시물_정보를_수정할_수_있다")
    public void 포스트의_포스트_ID를_통해_특정_게시물_정보를_수정할_수_있다() throws Exception {
        // given
        Long postId = 101L;

        PostUpdate postUpdate = PostUpdate.builder()
                .content("UPDATED content")
                .build();

        // when
        ResponseEntity<PostResponse> result = testContainer.postController.update(postId, postUpdate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody().getId()).isEqualTo(101L);
        assertThat(result.getBody().getContent()).isEqualTo("UPDATED content");
        assertThat(result.getBody().getModifiedAt()).isEqualTo(970402L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(100L);
    }

//    // TODO: 사용자가 존재하지 않는 게시물을 조회할 경우 에러가 난다
//    @Test
//    @DisplayName("사용자가 존재하지 않는 게시물을 조회할 경우 에러가 난다")
//    public void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다_mockMVC() throws Exception {
//        // given
//        // when
//        // then
//        mockMvc.perform(get("/api/posts/404"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Posts에서 ID 404를 찾을 수 없습니다."));
//    }
    @Test
    @DisplayName("사용자가 존재하지 않는 게시물을 조회할 경우 에러가 난다")
    public void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다_mockMVC() throws Exception {
        // given
        // when
        // then
        assertThatThrownBy(() -> {
            testContainer.postController.getById(404L);
        }).isInstanceOf(ResourceNotFoundException.class);
    }
}