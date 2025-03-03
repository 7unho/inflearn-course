//package com.april2nd.demo.post.controller;
//
//import com.april2nd.demo.post.domain.PostUpdate;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//class PostControllerTest {
//    /*
//        PostResponse = id, content, writer
//
//        test post (100, 'helloworld', 1678530673958, 0, 100);
//     */
//
//    // TODO: 포스트 ID를 통해 특정 게시물 정보를 불러올 수 있다
//    @Test
//    @DisplayName("포스트 ID를 통해 특정 게시물 정보를 불러올 수 있다")
//    public void 포스트_ID를_통해_특정_게시물_정보를_불러올_수_있다() throws Exception {
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
//
//    // TODO: 포스트 ID를 통해 특정 게시물 정보를 수정할 수 있다.
//    @Test
//    @DisplayName("포스트의_포스트_ID를_통해_특정_게시물_정보를_수정할_수_있다")
//    public void 포스트의_포스트_ID를_통해_특정_게시물_정보를_수정할_수_있다() throws Exception {
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
//
//    // TODO: 사용자가 존재하지 않는 게시물을 조회할 경우 에러가 난다
//    @Test
//    @DisplayName("사용자가 존재하지 않는 게시물을 조회할 경우 에러가 난다")
//    public void 사용자가_존재하지_않는_게시물을_조회할_경우_에러가_난다() throws Exception {
//        // given
//        // when
//        // then
//        mockMvc.perform(get("/api/posts/404"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Posts에서 ID 404를 찾을 수 없습니다."));
//    }
//}