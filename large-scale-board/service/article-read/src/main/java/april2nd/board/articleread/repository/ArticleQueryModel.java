package april2nd.board.articleread.repository;

import april2nd.board.articleread.client.ArticleClient;
import april2nd.board.common.event.payload.article.ArticleCreatedEventPayload;
import april2nd.board.common.event.payload.article.ArticleLikedEventPayload;
import april2nd.board.common.event.payload.article.ArticleUnlikedEventPayload;
import april2nd.board.common.event.payload.article.ArticleUpdatedEventPayload;
import april2nd.board.common.event.payload.comment.CommentCreatedEventPayload;
import april2nd.board.common.event.payload.comment.CommentDeletedEventPayload;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ArticleQueryModel {
    private Long articleId;
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long articleCommentCount;
    private Long articleLikeCount;

    public static ArticleQueryModel create(ArticleCreatedEventPayload payload) {
        return ArticleQueryModel.builder()
                .articleId(payload.getArticleId())
                .title(payload.getTitle())
                .content(payload.getContent())
                .boardId(payload.getBoardId())
                .writerId(payload.getWriterId())
                .createdAt(payload.getCreatedAt())
                .modifiedAt(payload.getModifiedAt())
                .articleCommentCount(0L)
                .articleLikeCount(0L)
                .build();
    }

    public static ArticleQueryModel create(ArticleClient.ArticleResponse article, Long commentCount, Long likeCount) {
        return ArticleQueryModel.builder()
                .articleId(article.getArticleId())
                .title(article.getTitle())
                .content(article.getContent())
                .boardId(article.getBoardId())
                .writerId(article.getWriterId())
                .createdAt(article.getCreatedAt())
                .modifiedAt(article.getModifiedAt())
                .articleCommentCount(commentCount)
                .articleLikeCount(likeCount)
                .build();
    }

    public void updateBy(CommentCreatedEventPayload payload) {
        this.articleCommentCount = payload.getArticleCommentCount();
    }

    public void updateBy(CommentDeletedEventPayload payload) {
        this.articleCommentCount = payload.getArticleCommentCount();
    }

    public void updateBy(ArticleLikedEventPayload payload) {
        this.articleLikeCount = payload.getArticleLikeCount();
    }

    public void updateBy(ArticleUnlikedEventPayload payload) {
        this.articleLikeCount = payload.getArticleLikeCount();
    }

    public void updateBy(ArticleUpdatedEventPayload payload) {
        this.title = payload.getTitle();
        this.content = payload.getContent();
        this.boardId = payload.getBoardId();
        this.writerId = payload.getWriterId();
        this.createdAt = payload.getCreatedAt();
        this.modifiedAt = payload.getModifiedAt();
    }
}
