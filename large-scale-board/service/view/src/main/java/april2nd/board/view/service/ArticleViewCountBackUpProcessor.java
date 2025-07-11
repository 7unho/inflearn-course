package april2nd.board.view.service;

import april2nd.board.common.event.EventType;
import april2nd.board.common.event.payload.article.ArticleViewedEventPayload;
import april2nd.board.common.outboxmessagerelay.OutboxEventPublisher;
import april2nd.board.view.entity.ArticleViewCount;
import april2nd.board.view.repository.ArticleViewCountBackUpRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackUpProcessor {
    private final OutboxEventPublisher outboxEventPublisher;
    private final ArticleViewCountBackUpRepository articleViewCountBackUpRepository;

    @Transactional
    public void backUp(Long articleId, Long viewCount) {
        int updatedRows = articleViewCountBackUpRepository.updateViewCount(articleId, viewCount);
        if (updatedRows == 0) {
            articleViewCountBackUpRepository.findById(articleId)
                    .ifPresentOrElse(ignored -> { },
                            () -> articleViewCountBackUpRepository.save(ArticleViewCount.init(articleId, viewCount))
                    );
        }

        outboxEventPublisher.publish(
                EventType.ARTICLE_VIEWED,
                ArticleViewedEventPayload.builder()
                        .articleId(articleId)
                        .articleViewCount(viewCount)
                        .build(),
                articleId
        );
    }
}
