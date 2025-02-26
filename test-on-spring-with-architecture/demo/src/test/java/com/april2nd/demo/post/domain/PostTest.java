package com.april2nd.demo.post.domain;

import com.april2nd.demo.mock.TestClockHolder;
import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PostTest {
    // TODO: PostCreate으로_게시물을_생성할_수_있다
    @Test
    public void PostCreate으로_게시물을_생성할_수_있다() throws Exception {
        // given
        PostCreate postCreate = PostCreate.builder()
                .writerId(100L)
                .content("hello test")
                .build();
//        values (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
        User writer = User.builder()
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .build();
        // when
        Post post = Post.from(postCreate, writer, new TestClockHolder(1678530673958L));

        // then
        assertThat(post.getContent()).isEqualTo("hello test");
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getWriter().getEmail()).isEqualTo("rlawnsgh8395@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("april2nd");
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaa-aaaaaaaaaa-aaaaa-aaaaa");
    }

    @Test
    public void PostUpdate으로_게시물을_수정할_수_있다() throws Exception {
        // given
        PostUpdate postUpdate = PostUpdate.builder()
                .content("hello test")
                .build();
//        values (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
        User writer = User.builder()
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .status(UserStatus.ACTIVE)
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .build();
        // when
        Post post = Post.builder()
                .id(100L)
                .content("helloworld")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(writer)
                .build();

        post = post.update(postUpdate, new TestClockHolder(1678530673958L));

        // then
        assertThat(post.getContent()).isEqualTo("hello test");
        assertThat(post.getCreatedAt()).isEqualTo(1678530673958L);
        assertThat(post.getWriter().getEmail()).isEqualTo("rlawnsgh8395@naver.com");
        assertThat(post.getWriter().getNickname()).isEqualTo("april2nd");
        assertThat(post.getWriter().getAddress()).isEqualTo("seoul");
        assertThat(post.getWriter().getStatus()).isEqualTo(UserStatus.ACTIVE);
        assertThat(post.getWriter().getCertificationCode()).isEqualTo("aaaaa-aaaaaaaaaa-aaaaa-aaaaa");
    }
}