package april2nd.board.hotarticle.service.eventhandler;

import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.payload.article.ArticleUnlikedEventPayload;
import april2nd.board.hotarticle.repository.ArticleLikeCountRepository;
import april2nd.board.hotarticle.utils.TimeCalculatorUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleUnlikedEventHandler implements EventHandler<ArticleUnlikedEventPayload> {
    private final ArticleLikeCountRepository articleLikeCountRepository;

    @Override
    public void handle(Event<ArticleUnlikedEventPayload> event) {
        articleLikeCountRepository.createOrUpdate(
                event.getPayload().getArticleId(),
                event.getPayload().getArticleLikeCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean supports(Event<ArticleUnlikedEventPayload> event) {
        return EventType.ARTICLE_UNLIKED == event.getEventType();
    }

    @Override
    public Long findArticleId(Event<ArticleUnlikedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
