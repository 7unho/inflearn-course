package com.april2nd.demo.post.service;

import com.april2nd.demo.common.domain.exception.ResourceNotFoundException;
import com.april2nd.demo.common.service.port.ClockHolder;
import com.april2nd.demo.mock.*;
import com.april2nd.demo.post.domain.Post;
import com.april2nd.demo.post.domain.PostCreate;
import com.april2nd.demo.post.domain.PostUpdate;
import com.april2nd.demo.user.domain.User;
import com.april2nd.demo.user.domain.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class PostServiceImplTest {
    private PostServiceImpl postServiceImpl;
    @BeforeEach
    void init() {
        /*
        insert into `posts` (`id`, `content`, `created_at`, `modified_at`, `user_id`)
        values (100, 'helloworld', 1678530673958, 0, 100);

        (100, 'rlawnsgh8395@naver.com', 'april2nd', 'seoul', 'aaaaa-aaaaaaaaaa-aaaaa-aaaaa', 'ACTIVE', 0);
         */
        FakePostRepository fakePostRepository = new FakePostRepository();
        FakeUserRepository fakeUserRepository = new FakeUserRepository();
        ClockHolder clockHolder = new TestClockHolder(1678530673958L);

        User writer = User.builder()
                .id(100L)
                .email("rlawnsgh8395@naver.com")
                .nickname("april2nd")
                .address("seoul")
                .certificationCode("aaaaa-aaaaaaaaaa-aaaaa-aaaaa")
                .status(UserStatus.ACTIVE)
                .lastLoginAt(970402L)
                .build();

        Post post = Post.builder()
                .id(100L)
                .content("helloworld")
                .createdAt(1678530673958L)
                .modifiedAt(0L)
                .writer(writer)
                .build();

        /*
        @deprecated
        UserService userService = UserService.builder()
                .certificationService(new CertificationService(fakeMailSender))
                .clockHolder(new TestClockHolder(970402L))
                .userRepository(fakeUserRepository)
                .uuidHolder(new TestUuidHolder("aaaaa-aaaaaaaaaa-aaaaa-aaaaa"))
                .build();
         */

        this.postServiceImpl = PostServiceImpl.builder()
                .userRepository(fakeUserRepository)
                .postRepository(fakePostRepository)
                .clockHolder(clockHolder)
                .build();

        fakeUserRepository.save(writer);
        fakePostRepository.save(post);
    }

    @Test
    @DisplayName("postId로 포스트를 불러올 수 있다")
    public void postId로_포스트를_불러올_수_있다() throws Exception {
        //given
        //when
        Post result = postServiceImpl.getById(100L);
        //then
        assertThat(result.getId()).isEqualTo(100L);
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
            postServiceImpl.getById(111L);
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
        Post result = postServiceImpl.create(postCreateDto);

        //then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getContent()).isEqualTo("createdByPostCreateDto");
        assertThat(result.getWriter().getId()).isEqualTo(100L);

        // TODO: 테스트 가능한 설계로 변경하기 ( PostServiceTest.create )
//        assertThat(result.getCreatedAt()).isGreaterThan(0L);
        assertThat(result.getCreatedAt()).isEqualTo(1678530673958L);
    }

    @Test
    @DisplayName("PostUpdateDto로 포스트를 수정할 수 있다")
    public void PostUpdateDto로_포스트를_수정할_수_있다() throws Exception {
        //given
        PostUpdate postUpdateDto = PostUpdate.builder()
                .content("UpdatedPostByPostUpdateDto")
                .build();
        //when
        postServiceImpl.update(100L, postUpdateDto);

        //then
        Post result = postServiceImpl.getById(100L);
        assertThat(result.getContent()).isEqualTo(postUpdateDto.getContent());
        // TODO: 테스트 가능한 설계로 변경하기 ( PostServiceTest.update )
//        assertThat(result.getModifiedAt()).isGreaterThan(0L);
        assertThat(result.getModifiedAt()).isEqualTo(1678530673958L);
    }
}