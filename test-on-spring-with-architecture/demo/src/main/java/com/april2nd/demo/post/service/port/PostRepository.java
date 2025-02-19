package com.april2nd.demo.post.service.port;

import com.april2nd.demo.post.domain.Post;

import java.util.Optional;

public interface PostRepository {

    Post save(Post post);

    Optional<Post> findById(long id);
}
