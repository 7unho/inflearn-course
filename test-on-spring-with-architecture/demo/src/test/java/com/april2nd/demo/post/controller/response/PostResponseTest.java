package com.april2nd.demo.post.controller.response;

import com.april2nd.demo.post.domain.Post;
import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostResponseTest {
    // TODO: Post로_응답(PostResponse)을_생성할_수_있다
    @Test
    public void Post로_응답_객체를_생성할_수_있다() throws Exception {
        // given
        User writer = User.builder()
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .build();
        Post post = Post.builder()
                .content("hello content")
                .writer(writer)
                .build();
        // when
        PostResponse postResponse = PostResponse.from(post);

        // then
        assertThat(postResponse.getContent()).isEqualTo("hello content");
        assertThat(postResponse.getWriter().getEmail()).isEqualTo("rlawnsgh8395@naver.com");
        assertThat(postResponse.getWriter().getNickname()).isEqualTo("april2nd");
        assertThat(postResponse.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
    }
}