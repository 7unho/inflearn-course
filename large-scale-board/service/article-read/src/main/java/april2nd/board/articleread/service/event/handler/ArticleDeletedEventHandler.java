package april2nd.board.articleread.service.event.handler;

import april2nd.board.articleread.repository.ArticleQueryModelRepository;
import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.payload.article.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        articleQueryModelRepository.delete(event.getPayload().getArticleId());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> payload) {
        return EventType.ARTICLE_DELETED == payload.getEventType();
    }
}
