package april2nd.board.hotarticle.service;

import april2nd.board.hotarticle.repository.ArticleCommentCountRepository;
import april2nd.board.hotarticle.repository.ArticleLikeCountRepository;
import april2nd.board.hotarticle.repository.ArticleViewCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HotArticleScoreCalculator {
    private final ArticleLikeCountRepository articleLikeCountRepository;
    private final ArticleViewCountRepository articleViewCountRepository;
    private final ArticleCommentCountRepository articleCommentCountRepository;

    private static final long ARTICLE_LIKE_COUNT_WEIGHT = 3;
    private static final long ARTICLE_VIEW_COUNT_WEIGHT = 1;
    private static final long ARTICLE_COMMENT_COUNT_WEIGHT = 2;

    public long calculate(Long articleId) {
        Long articleLikeCount = articleLikeCountRepository.read(articleId);
        Long articleViewCount = articleViewCountRepository.read(articleId);
        Long articleCommentCount = articleCommentCountRepository.read(articleId);

        if (articleLikeCount == null || articleViewCount == null || articleCommentCount == null) {
            throw new IllegalStateException("Failed to read counts for article ID: " + articleId);
        }

        return (articleLikeCount * ARTICLE_LIKE_COUNT_WEIGHT) +
               (articleViewCount * ARTICLE_VIEW_COUNT_WEIGHT) +
               (articleCommentCount * ARTICLE_COMMENT_COUNT_WEIGHT);
    }
}
