package april2nd.board.articleread.service.event.handler;

import april2nd.board.articleread.repository.ArticleQueryModelRepository;
import april2nd.board.common.event.Event;
import april2nd.board.common.event.EventType;
import april2nd.board.common.event.payload.article.ArticleViewedEventPayload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ArticleViewedEventHandler implements EventHandler<ArticleViewedEventPayload> {
    private final ArticleQueryModelRepository articleQueryModelRepository;
    @Override
    public void handle(Event<ArticleViewedEventPayload> event) {
        articleQueryModelRepository.read(event.getPayload().getArticleId())
                .ifPresent(articleQueryModel -> {

                });
    }

    @Override
    public boolean supports(Event<ArticleViewedEventPayload> event) {
        return EventType.ARTICLE_VIEWED == event.getEventType();
    }
}
