package com.april2nd.demo.post.infrastructure;

import com.april2nd.demo.post.domain.Post;
import com.april2nd.demo.user.infrastructure.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "created_at")
    private Long createdAt;

    @Column(name = "modified_at")
    private Long modifiedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity writer;

    // domain은 infrastructure layer의 정보를 모르는 것이 좋다.
    // 따라서 Post.toEntity의 형태가 아닌 PostEntity.fromModel의 형태로 사용
    public static PostEntity fromModel(Post post) {
        PostEntity postEntity = new PostEntity();
        postEntity.id = post.getId();
        postEntity.content = post.getContent();
        postEntity.createdAt = post.getCreatedAt();
        postEntity.modifiedAt = post.getModifiedAt();
        postEntity.writer = UserEntity.from(post.getWriter());
        return postEntity;
    }

    public Post toModel() {
        return Post.builder()
                .id(id)
                .content(content)
                .createdAt(createdAt)
                .modifiedAt(modifiedAt)
                .writer(writer.toModel())
                .build();
    }
}