package april2nd.board.comment.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Table(name = "comment_v2")
@Entity
@Getter
public class CommentV2 {
    @Id
    private Long commentId;
    private String content;
    private Long articleId;
    private Long writerId;
    @Embedded
    private CommentPath commentPath;
    private Boolean deleted;
    private LocalDateTime createAt;

    public static CommentV2 create(
            Long commentId,
            String content,
            Long articleId,
            Long writerId,
            CommentPath commentPath) {
        CommentV2 comment = new CommentV2();
        comment.commentId = commentId;
        comment.content = content;
        comment.articleId = articleId;
        comment.writerId = writerId;
        comment.commentPath = commentPath;
        comment.deleted = false;
        comment.createAt = LocalDateTime.now();

        return comment;
    }

    public boolean isRoot() {
        return commentPath.isRoot();
    }

    public void delete() {
        deleted = true;
    }
}