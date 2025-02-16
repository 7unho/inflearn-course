package com.april2nd.demo.post.service;

import com.april2nd.demo.common.domain.exception.ResourceNotFoundException;
import com.april2nd.demo.post.domain.PostCreate;
import com.april2nd.demo.post.domain.PostUpdate;
import com.april2nd.demo.post.infrastructure.PostEntity;
import com.april2nd.demo.post.infrastructure.PostRepository;
import com.april2nd.demo.user.infrastructure.UserEntity;
import java.time.Clock;

import com.april2nd.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserService userService;

    public PostEntity getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    public PostEntity create(PostCreate postCreate) {
        UserEntity userEntity = userService.getById(postCreate.getWriterId());
        PostEntity postEntity = new PostEntity();
        postEntity.setWriter(userEntity);
        postEntity.setContent(postCreate.getContent());
        postEntity.setCreatedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }

    public PostEntity update(long id, PostUpdate postUpdate) {
        PostEntity postEntity = getPostById(id);
        postEntity.setContent(postUpdate.getContent());
        postEntity.setModifiedAt(Clock.systemUTC().millis());
        return postRepository.save(postEntity);
    }
}