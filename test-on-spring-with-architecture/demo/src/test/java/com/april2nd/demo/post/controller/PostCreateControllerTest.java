package com.april2nd.demo.post.controller;

import com.april2nd.demo.mock.TestContainer;
import com.april2nd.demo.post.controller.response.PostResponse;
import com.april2nd.demo.post.domain.PostCreate;
import com.april2nd.demo.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class PostCreateControllerTest {
    //    @Test
//    @DisplayName("사용자는 게시물을 작성할 수 있다")
//    public void 사용자는_게시물을_작성할_수_있다_mockMVC() throws Exception {
//        // given
//        Long writerId = 100L;
//        String content = "CREATED post";
//        PostCreate postCreate = PostCreate.builder()
//                .writerId(writerId)
//                .content(content)
//                .build();
//
//        String requestBody = objectMapper.writeValueAsString(postCreate);
//        // when
//        // then
//        mockMvc.perform(
//                        post("/api/posts")
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(requestBody)
//                ).andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.content").value(content))
//                .andExpect(jsonPath("$.createdAt").exists())
//                .andExpect(jsonPath("$.modifiedAt").doesNotExist())
//                .andExpect(jsonPath("$.writer.id").value(writerId));
//    }
    @Test
    @DisplayName("사용자는 게시물을 작성할 수 있다")
    public void 사용자는_게시물을_작성할_수_있다() throws Exception {
        // given
        TestContainer testContainer = TestContainer.builder()
                .clockHolder(() -> 970402L)
                .build();

        User writer = User.builder()
                .id(100L)
                .build();

        testContainer.userRepository.save(writer);

        PostCreate postCreate = PostCreate.builder()
                .content("CREATED content")
                .writerId(writer.getId())
                .build();
        // when
        ResponseEntity<PostResponse> result = testContainer.postCreateController.createPost(postCreate);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(result.getBody().getId()).isEqualTo(1L);
        assertThat(result.getBody().getContent()).isEqualTo("CREATED content");
        assertThat(result.getBody().getCreatedAt()).isEqualTo(970402L);
        assertThat(result.getBody().getWriter().getId()).isEqualTo(100L);
    }
}
