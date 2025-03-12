package com.april2nd.demo.post.service;

import com.april2nd.demo.common.domain.exception.ResourceNotFoundException;
import com.april2nd.demo.common.service.port.ClockHolder;
import com.april2nd.demo.post.domain.Post;
import com.april2nd.demo.post.domain.PostCreate;
import com.april2nd.demo.post.domain.PostUpdate;
import com.april2nd.demo.post.service.port.PostRepository;
import com.april2nd.demo.post.controller.port.PostService;
import com.april2nd.demo.user.domain.User;

import com.april2nd.demo.user.service.port.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Builder
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ClockHolder clockHolder;

    public Post getById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Posts", id));
    }

    @Transactional
    public Post create(PostCreate postCreate) {
        User writer = userRepository.getById(postCreate.getWriterId());
        Post post = Post.from(postCreate, writer, clockHolder);
        return postRepository.save(post);
    }

    @Transactional
    public Post update(long id, PostUpdate postUpdate) {
        Post post = getById(id);
        post = post.update(postUpdate, clockHolder);
        return postRepository.save(post);
    }
}