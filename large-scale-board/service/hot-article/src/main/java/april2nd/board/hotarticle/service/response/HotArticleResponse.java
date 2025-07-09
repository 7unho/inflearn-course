package april2nd.board.hotarticle.service.response;

import april2nd.board.hotarticle.client.ArticleClient;

import java.time.LocalDateTime;

public record HotArticleResponse(
        Long articleId,
        String title,
        LocalDateTime createdAt
) {
    public static HotArticleResponse from(ArticleClient.ArticleResponse articleResponse) {
        return new HotArticleResponse(
                articleResponse.getArticleId(),
                articleResponse.getTitle(),
                articleResponse.getCreatedAt()
        );
    }
}
