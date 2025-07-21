package april2nd.board.articleread.service.event.handler;

import april2nd.board.articleread.repository.ArticleIdListRepository;
import april2nd.board.articleread.repository.ArticleQueryModelRepository;
import april2nd.board.articleread.repository.BoardArticleCountRepository;
import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.payload.article.ArticleDeletedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final ArticleIdListRepository articleIdListRepository;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = getPayload(event);
        articleQueryModelRepository.delete(payload.getArticleId());
        articleIdListRepository.delete(payload.getBoardId(), payload.getArticleId());
        boardArticleCountRepository.createOrUpdate(payload.getBoardId(), payload.getBoardArticleCount());
    }

    private static ArticleDeletedEventPayload getPayload(Event<ArticleDeletedEventPayload> event) {
        return event.getPayload();
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> payload) {
        return EventType.ARTICLE_DELETED == payload.getEventType();
    }
}
