package com.april2nd.demo.post.service.port;

import com.april2nd.demo.post.infrastructure.PostEntity;

import java.util.Optional;

public interface PostRepository {

    PostEntity save(PostEntity postEntity);

    Optional<PostEntity> findById(long id);
}
