package april2nd.board.like.service.response;

import april2nd.board.like.entity.ArticleLike;

import java.time.LocalDateTime;

public record ArticleLikeResponse(
        Long articleLikeId,
        Long articleId,
        Long userId,
        LocalDateTime createdAt
) {
    public static ArticleLikeResponse from(ArticleLike articleLike) {
        return new ArticleLikeResponse(
                articleLike.getArticleLikeId(),
                articleLike.getArticleId(),
                articleLike.getUserId(),
                articleLike.getCreatedAt()
        );
    }
}
