package april2nd.board.comment.service.response;

import april2nd.board.comment.entity.Comment;
import april2nd.board.comment.entity.CommentV2;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class CommentResponse {
    private Long commentId;
    private String content;
    private Long parentCommentId;
    private Long articleId;
    private Long writerId;
    private String path;
    private Boolean deleted;
    private LocalDateTime createdAt;

    public static CommentResponse from(Comment comment) {
        CommentResponse response = new CommentResponse();
        response.commentId = comment.getCommentId();
        response.parentCommentId = comment.getParentCommentId();
        response.content = comment.getContent();
        response.articleId = comment.getArticleId();
        response.writerId = comment.getWriterId();
        response.deleted = comment.getDeleted();
        response.createdAt = comment.getCreatedAt();

        return response;
    }

    public static CommentResponse from(CommentV2 comment) {
        CommentResponse response = new CommentResponse();
        response.commentId = comment.getCommentId();
        response.content = comment.getContent();
        response.articleId = comment.getArticleId();
        response.writerId = comment.getWriterId();
        response.path = comment.getCommentPath().toString();
        response.deleted = comment.getDeleted();
        response.createdAt = comment.getCreatedAt();

        return response;
    }
}
