package com.april2nd.demo.post.controller.port;

import com.april2nd.demo.post.domain.Post;
import com.april2nd.demo.post.domain.PostCreate;
import com.april2nd.demo.post.domain.PostUpdate;

public interface PostService {
    Post getById(long id);

    Post create(PostCreate postCreate);

    Post update(long id, PostUpdate postUpdate);
}
