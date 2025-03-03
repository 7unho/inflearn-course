//package com.april2nd.demo.post.controller;
//
//import com.april2nd.demo.post.domain.PostCreate;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.http.MediaType;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class PostCreateControllerTest {
//    @Test
//    @DisplayName("사용자는 게시물을 작성할 수 있다")
//    public void 사용자는_게시물을_작성할_수_있다() throws Exception {
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
//}